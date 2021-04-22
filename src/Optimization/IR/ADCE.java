package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.br;
import LLVMIR.Instruction.phi;
import LLVMIR.Instruction.statement;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;

import java.util.*;

public class ADCE implements pass {
	private final IREntry programIREntry;
	private sideEffectAnalyzer sideEffectProperty;

	public ADCE(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private boolean run(function func) {
		basicBlock entry = new basicBlock(), exit = new basicBlock();
		Set<basicBlock> nodes = new HashSet<>(func.blocks);
		nodes.add(entry);
		nodes.add(exit);
		dominanceAnalyser dominanceProperty = new dominanceAnalyser(exit, nodes);
		dominanceProperty.addEdge(func.blocks.iterator().next(), entry);
		func.blocks.forEach(blk -> {
			if (blk.successors().isEmpty()) dominanceProperty.addEdge(exit, blk);
			else blk.successors().forEach(sucBlk -> dominanceProperty.addEdge(sucBlk, blk));
		});

		boolean changed = false;
		if (dominanceProperty.dominanceAnalysis(true)) {
			Map<register, Set<basicBlock>> def = new HashMap<>();
			func.variablesAnalysis(null, null, null, null, def);
			Set<statement> live = new HashSet<>(), workList = new HashSet<>();
			func.blocks.forEach(blk -> blk.stmts.stream()
				.filter(stmt -> sideEffectProperty.sideEffectStatements.contains(stmt))
				.forEach(stmt -> {
					live.add(stmt);
					workList.add(stmt);
				})
			);
			dominanceProperty.DF.get(exit).stream()
				.filter(blk -> blk.tailStmt instanceof br && !live.contains(blk.tailStmt))
				.forEach(blk -> {
					workList.add(blk.tailStmt);
					live.add(blk.tailStmt);
				});
			func.blocks.stream()
				.filter(blk -> blk.tailStmt instanceof br && ((br) blk.tailStmt).cond == null)
				.forEach(blk -> {
					workList.add(blk.tailStmt);
					live.add(blk.tailStmt);
				});
			while (!workList.isEmpty()) {
				statement stmt = workList.iterator().next();
				workList.remove(stmt);
				stmt.uses().stream().filter(v -> v.def != null && !live.contains(v.def)).forEach(v -> {
					workList.add(v.def);
					live.add(v.def);
				});
				if (stmt instanceof phi)
					dominanceProperty.adj.get(stmt.belongTo).stream()
						.filter(blk -> !live.contains(blk.tailStmt))
						.forEach(blk -> {
							workList.add(blk.tailStmt);
							live.add(blk.tailStmt);
						});
				dominanceProperty.DF.get(stmt.belongTo).stream()
					.filter(blk -> blk.tailStmt instanceof br && !live.contains(blk.tailStmt))
					.forEach(blk -> {
						workList.add(blk.tailStmt);
						live.add(blk.tailStmt);
					});
			}
			List<basicBlock> blocks = new ArrayList<>(func.blocks);
			for (basicBlock blk : blocks) {
				List<statement> stmts = new ArrayList<>(blk.stmts);
				changed |= stmts.stream().anyMatch(stmt -> !live.contains(stmt));
				stmts.stream().filter(stmt -> !live.contains(stmt)).forEach(blk::removeInstruction);
			}
		}
		return changed;
	}

	@Override
	public boolean run() {
		sideEffectProperty = new sideEffectAnalyzer(programIREntry);
		sideEffectProperty.run();
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
