package Backend;

import LLVMIR.IREntry;
import LLVMIR.Instruction._move;
import LLVMIR.Instruction.phi;
import LLVMIR.Operand.register;
import LLVMIR.Operand.undefinedValue;
import LLVMIR.basicBlock;
import LLVMIR.function;
import Optimization.IR.Analyser.dominanceAnalyser;

import java.util.*;

public class SSAConstructor implements pass {
	private final IREntry programIREntry;

	private dominanceAnalyser dominanceProperty;

	private Set<register> vars;
	private final Map<register, Integer> renamingCounter = new LinkedHashMap<>();

	public SSAConstructor(IREntry programIREntry) {
		this.programIREntry = programIREntry;
	}

	@Override
	public boolean run() {
		programIREntry.functions.stream().filter(func -> func.blocks != null).forEach(func -> {
			dominanceProperty = new dominanceAnalyser(func.blocks.iterator().next(), new LinkedHashSet<>(func.blocks));
			func.blocks.forEach(u -> u.successors().forEach(v -> dominanceProperty.addEdge(u, v)));
			dominanceProperty.dominanceAnalysis(true);
			Set<basicBlock> unreachableBlocks = new LinkedHashSet<>(func.blocks);
			dominanceProperty.getPostOrderOfGraph().forEach(unreachableBlocks::remove);
			func.blocks.removeAll(unreachableBlocks);
			phiInsertion(func);
			variableRenaming(func);
		});
		return true;
	}

	private void phiInsertion(function func) {
		Map<register, Set<basicBlock>> defs = new LinkedHashMap<>();
		vars = new LinkedHashSet<>();
		func.variablesAnalysis(vars, null, null, null, defs);
		Set<register> vars_ = new LinkedHashSet<>(vars);
		func.argValues.forEach(vars_::remove);
		vars_.forEach(v -> func.blocks.iterator().next().push_front(new _move(new undefinedValue(v.type), v)));
		vars.stream().filter(v -> !v.isLocalVariable).forEach(v -> {
			renamingCounter.put(v, 0);
			Set<basicBlock> F = new LinkedHashSet<>(), W = new LinkedHashSet<>(defs.get(v));
			while (!W.isEmpty()) {
				basicBlock x = W.iterator().next();
				W.remove(x);
				dominanceProperty.DF.get(x).stream().filter(y -> !F.contains(y)).forEach(y -> {
					y.addPhiFunction(v, new LinkedList<>(dominanceProperty.radj.get(y)));
					F.add(y);
					if (!defs.get(v).contains(y)) W.add(y);
				});
			}
		});
	}

	private void variableRenaming(function func) {
		vars.forEach(v -> v.reachingDef = null);
		for (int i = 0;i < func.argValues.size();++ i) {
			register argv = func.argValues.get(i), argv_ = new register(argv.type, "_r." + argv.name + "." + renamingCounter.get(argv));
			renamingCounter.put(argv, renamingCounter.get(argv) + 1);
			func.argValues.set(i, argv_);
			argv_.reachingDef = argv.reachingDef;
			argv.reachingDef = argv_;
		}
		dominanceProperty.getPreOrderOfTree().forEach(blk -> {
			blk.stmts.forEach(i -> {
				if (!(i instanceof phi))
					i.uses().forEach(v -> {
						updateReachingDef(v, i.belongTo);
						i.replaceUse(v, v.reachingDef);
					});
				i.defs().forEach(v -> {
					updateReachingDef(v, i.belongTo);
					register v_ = new register(v.type, "_r." + v.name + "." + renamingCounter.get(v));
					renamingCounter.put(v, renamingCounter.get(v) + 1);
					i.replaceDef(v, v_);
					v_.def = i;
					v_.reachingDef = v.reachingDef;
					v.reachingDef = v_;
				});
			});
			blk.successors().forEach(sucBlk -> sucBlk.phiCollections.forEach((u, phiInst) -> {
				updateReachingDef(u, blk);
				phiInst.replaceUse(u, u.reachingDef, blk);
			}));
		});
	}

	private void updateReachingDef(register v, basicBlock b) {
		register r = v.reachingDef;
		while (!(r == null || r.def == null || dominanceProperty.isDominatedBy(b, r.def.belongTo)))
			r = r.reachingDef;
		v.reachingDef = r;
	}
}
