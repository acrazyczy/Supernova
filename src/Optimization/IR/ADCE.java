package Optimization.IR;

import Backend.pass;
import LLVMIR.IREntry;
import LLVMIR.Instruction.br;
import LLVMIR.Instruction.phi;
import LLVMIR.Instruction.statement;
import LLVMIR.Operand.register;
import LLVMIR.basicBlock;
import LLVMIR.function;
import Optimization.IR.Analyser.dominanceAnalyser;
import Optimization.IR.Analyser.sideEffectAnalyzer;

import java.util.*;
import java.util.stream.Collectors;

public class ADCE implements pass {
	private final IREntry programIREntry;
	private sideEffectAnalyzer sideEffectProperty;

	public ADCE(IREntry programIREntry) {this.programIREntry = programIREntry;}

	private boolean run(function func) {
		basicBlock entry = new basicBlock(), exit = new basicBlock();
		Set<basicBlock> nodes = new LinkedHashSet<>(func.blocks);
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
			Map<register, Set<basicBlock>> def = new LinkedHashMap<>();
			func.variablesAnalysis(null, null, null, null, def);
			Set<statement> live = new LinkedHashSet<>(), workList = new LinkedHashSet<>();
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
			/*
			func.blocks.stream()
				.filter(blk -> blk.tailStmt instanceof br && ((br) blk.tailStmt).cond == null)
				.forEach(blk -> {
					workList.add(blk.tailStmt);
					live.add(blk.tailStmt);
				});
			*/
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
			for (basicBlock blk : func.blocks) {
				List<statement> stmts = new ArrayList<>(blk.stmts);
				changed |= stmts.stream().anyMatch(stmt -> (!isUnconditionalJump(stmt) || stmt.belongTo.stmts.size() == 1) && !live.contains(stmt));
				stmts.stream().filter(stmt -> (!isUnconditionalJump(stmt) || stmt.belongTo.stmts.size() == 1) && !live.contains(stmt)).forEach(stmt -> blk.removeInstruction(stmt, true));
			}
			func.blocks.stream()
				.filter(blk -> !blk.stmts.isEmpty())
				.collect(Collectors.toList()).forEach(blk -> {
				if (blk.tailStmt instanceof br) {
					br tailStmt = (br) blk.tailStmt;
					tailStmt.trueBranch = jumpReplacement(tailStmt.trueBranch, blk, blk, new LinkedHashSet<>(), dominanceProperty.radj);
					if (tailStmt.cond != null) tailStmt.falseBranch = jumpReplacement(tailStmt.falseBranch, blk, blk, new LinkedHashSet<>(), dominanceProperty.radj);
				}
			});
			new LinkedHashSet<>(func.blocks).stream().filter(blk -> blk.stmts.isEmpty()).forEach(func.blocks::remove);
		}
		return changed;
	}

	private boolean isUnconditionalJump(statement stmt) {return stmt instanceof br && ((br) stmt).cond == null;}

	private basicBlock jumpReplacement(basicBlock blk, basicBlock preBlk, basicBlock fromBlk, Set<basicBlock> isVisited, Map<basicBlock, Set<basicBlock>> successors) {
		if (isVisited.contains(blk)) return null;
		isVisited.add(blk);
		if (!blk.stmts.isEmpty()) {
			blk.replacePredecessor(preBlk, fromBlk);
			return blk;
		}
		return successors.get(blk).stream().map(sucBlk -> jumpReplacement(sucBlk, blk, fromBlk, isVisited, successors)).reduce(null, (a, b) -> b != null ? b : a);
	}

	@Override
	public boolean run() {
		sideEffectProperty = new sideEffectAnalyzer(programIREntry);
		sideEffectProperty.run();
		return programIREntry.functions.stream().filter(func -> func.blocks != null).map(this::run).reduce(false, (a, b) -> a || b);
	}
}
