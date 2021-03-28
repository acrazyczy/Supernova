package Backend;

import Assembly.Instruction.*;
import Assembly.Operand.*;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;
import Assembly.stackFrame;
import LLVMIR.IREntry;
import LLVMIR.Instruction.statement;
import LLVMIR.Operand.*;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMStructureType;
import LLVMIR.basicBlock;
import LLVMIR.Instruction.*;
import LLVMIR.function;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.compareUnsigned;

public class instSelector implements pass {
	private final IREntry programIREntry;
	private final asmEntry programAsmEntry;
	private int blockCounter = 0;
	private int virtualRegCounter = 0;

	private asmFunction currentFunction;
	private asmBlock currentBlock;

	private final HashMap<register, virtualReg> regMap = new HashMap<>();
	private final HashMap<basicBlock, asmBlock> blkMap = new HashMap<>();
	static physicalReg
		sp = physicalReg.pRegs.get("sp"),
		a0 = physicalReg.pRegs.get("a0"),
		zero = physicalReg.pRegs.get("zero");

	private virtualReg registerMapping(register reg) {
		if (!regMap.containsKey(reg)) regMap.put(reg, new virtualReg(++ virtualRegCounter));
		return regMap.get(reg);
	}

	private asmBlock blockMapping(basicBlock blk) {
		if (!blkMap.containsKey(blk)) blkMap.put(blk, new asmBlock(++ blockCounter));
		return blkMap.get(blk);
	}

	public instSelector(IREntry programIREntry, asmEntry programAsmEntry) {
		this.programIREntry = programIREntry;
		this.programAsmEntry = programAsmEntry;
	}

	private void buildAsmInst(statement stmt) {
		if (stmt instanceof _move) {
			_move stmt_ = (_move) stmt;
			reg rd = registerMapping((register) stmt_.dest);
			if (stmt_.src instanceof integerConstant) currentBlock.addInst(new liInst(rd, new intImm(((integerConstant) stmt_.src).val)));
			else if (stmt_.src instanceof booleanConstant) currentBlock.addInst(new liInst(rd, new intImm(((booleanConstant) stmt_.src).val)));
			else if (stmt_.src instanceof nullPointerConstant) currentBlock.addInst(new liInst(rd, new intImm(0)));
			else if (stmt_.src instanceof register) currentBlock.addInst(new mvInst(rd, registerMapping((register) stmt_.src)));
			else {
				assert stmt_.src instanceof globalVariable;
				globalData glb = programAsmEntry.gblMapping.get(stmt_.src);
				currentBlock.addInst(new luiInst(rd, new relocationImm(relocationImm.type.hi, glb)));
				currentBlock.addInst(new loadInst(loadInst.loadType.lw, rd, rd, new relocationImm(relocationImm.type.lo, glb)));
			}
		} else if (stmt instanceof binary) {
			binary stmt_ = (binary) stmt;
			entity lhs = stmt_.op1, rhs = stmt_.op2;
			reg rd = registerMapping((register) stmt_.dest);
			binary.instCode inst = stmt_.inst;
			if (inst == binary.instCode.sub || inst == binary.instCode.sdiv || inst == binary.instCode.srem ||
				inst == binary.instCode.shl || inst == binary.instCode.ashr || inst == binary.instCode.mul) {
				reg rs1, rs2;
				if (lhs instanceof register) rs1 = registerMapping((register) lhs);
				else {
					assert lhs instanceof integerConstant || lhs instanceof booleanConstant;
					int val = lhs instanceof integerConstant ? ((integerConstant) lhs).val : ((booleanConstant) lhs).val;
					rs1 = new virtualReg(++ virtualRegCounter);
					currentBlock.addInst(new liInst(rs1, new intImm(val)));
				}
				if (rhs instanceof register) rs2 = registerMapping((register) rhs);
				else {
					assert rhs instanceof integerConstant || rhs instanceof booleanConstant;
					int val = rhs instanceof integerConstant ? ((integerConstant) rhs).val : ((booleanConstant) rhs).val;
					rs2 = new virtualReg(++ virtualRegCounter);
					currentBlock.addInst(new liInst(rs2, new intImm(val)));
				}
				RTypeInst.opType type = null;
				switch (inst) {
					case sub -> type = RTypeInst.opType.sub;
					case sdiv -> type = RTypeInst.opType.div;
					case srem -> type = RTypeInst.opType.rem;
					case shl -> type = RTypeInst.opType.sll;
					case ashr -> type = RTypeInst.opType.sra;
					case mul -> type = RTypeInst.opType.mul;
				}
				currentBlock.addInst(new RTypeInst(type, rd, rs1, rs2));
			} else {
				if (rhs instanceof register) {
					entity tmp = lhs;
					lhs = rhs;
					rhs = tmp;
				}
				if (lhs instanceof register) {
					reg lhs_ = registerMapping((register) lhs);
					if (rhs instanceof register) {
						reg rhs_ = registerMapping((register) rhs);
						RTypeInst.opType type = null;
						switch (inst) {
							case or -> type = RTypeInst.opType.or;
							case add -> type = RTypeInst.opType.add;
							case and -> type = RTypeInst.opType.and;
							case xor -> type = RTypeInst.opType.xor;
						}
						currentBlock.addInst(new RTypeInst(type, rd, lhs_, rhs_));
					} else {
						assert rhs instanceof integerConstant || rhs instanceof booleanConstant;
						intImm intImm = new intImm(rhs instanceof integerConstant ? ((integerConstant) rhs).val : ((booleanConstant) rhs).val);
						ITypeInst.opType type = null;
						switch (inst) {
							case or -> type = ITypeInst.opType.ori;
							case add -> type = ITypeInst.opType.addi;
							case and -> type = ITypeInst.opType.andi;
							case xor -> type = ITypeInst.opType.xori;
						}
						currentBlock.addInst(new ITypeInst(type, rd, lhs_, intImm));
					}
				} else {
					assert (lhs instanceof integerConstant || lhs instanceof booleanConstant) &&
						(rhs instanceof integerConstant || rhs instanceof booleanConstant);
					int lhs_ = lhs instanceof integerConstant ? ((integerConstant) lhs).val : ((booleanConstant) lhs).val;
					int rhs_ = rhs instanceof integerConstant ? ((integerConstant) rhs).val : ((booleanConstant) rhs).val;
					switch (inst) {
						case add -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ + rhs_)));
						case and -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ & rhs_)));
						case or -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ | rhs_)));
						case xor -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ ^ rhs_)));
					}
				}
			}
		} else if (stmt instanceof bitcast) {
			bitcast stmt_ = (bitcast) stmt;
			reg rd = registerMapping((register) stmt_.dest);
			if (stmt_.value instanceof globalVariable) currentBlock.addInst(new laInst(rd, programAsmEntry.gblMapping.get(stmt_.value)));
			else currentBlock.addInst(new mvInst(rd, registerMapping((register) stmt_.value)));
		} else if (stmt instanceof br) {
			br stmt_ = (br) stmt;
			asmBlock trueBlk = blkMap.get(stmt_.trueBranch);
			if (stmt_.cond == null) currentBlock.addInst(new jumpInst(trueBlk));
			else {
				asmBlock falseBlk = blkMap.get(stmt_.falseBranch);
				if (stmt_.cond instanceof booleanConstant)
					if (((booleanConstant) stmt_.cond).val == 1) currentBlock.addInst(new jumpInst(trueBlk));
					else currentBlock.addInst(new jumpInst(falseBlk));
				else {
					reg cond = registerMapping((register) stmt_.cond);
					boolean merged = false;
					inst tailInst = currentBlock.tailInst;
					if (tailInst != null)
						if (tailInst instanceof szInst) {
							if (((szInst) tailInst).testMergeability(cond)) {
								currentBlock.tailInst = new brInst(
									((szInst) tailInst).type == szInst.opType.seqz ? brInst.opType.bnez : brInst.opType.beqz,
									((szInst) tailInst).rs, null, falseBlk
								);
								merged = true;
							}
						} else if (tailInst instanceof RTypeInst) {
							if (((RTypeInst) tailInst).testMergeability(cond)) {
								if (((RTypeInst) tailInst).type == RTypeInst.opType.slt)
									currentBlock.tailInst = new brInst(
										brInst.opType.bge,
										((RTypeInst) tailInst).rs1, ((RTypeInst) tailInst).rs2, falseBlk
									);
								else currentBlock.tailInst = new brInst(
										brInst.opType.bgeu,
										((RTypeInst) tailInst).rs1, ((RTypeInst) tailInst).rs2, falseBlk
									);
								merged = true;
							}
						} else if (tailInst instanceof ITypeInst && tailInst.pre instanceof RTypeInst) {
							RTypeInst preInst = (RTypeInst) tailInst.pre;
							if (((ITypeInst) tailInst).testMergeability(cond) && preInst.testMergeability(cond)) {
								if (preInst.type == RTypeInst.opType.slt) currentBlock.tailInst = new brInst(brInst.opType.blt, preInst.rs1, preInst.rs2, falseBlk);
								else currentBlock.tailInst = new brInst(brInst.opType.bltu, preInst.rs1, preInst.rs2, falseBlk);
								currentBlock.tailInst.pre = preInst.pre;
								if (preInst.pre != null) preInst.suf = currentBlock.tailInst;
								else currentBlock.headInst = currentBlock.tailInst;
								merged = true;
							}
						}
					if (!merged) currentBlock.addInst(new brInst(brInst.opType.beqz, cond, null, falseBlk));
				}
			}
		} else if (stmt instanceof call) {
			call stmt_ = (call) stmt;
			HashMap<String, virtualReg> virRegs = new HashMap<>();
			// store caller save registers
			physicalReg.callerSavePRegs.forEach((name, phyReg) -> {
				virtualReg virReg = new virtualReg(++ virtualRegCounter);
				currentBlock.addInst(new mvInst(virReg, phyReg));
				virRegs.put(name, virReg);
			});
			for (int i = stmt_.callee.argValues.size() - 1;i >= 0;-- i) {
				entity paraEntity = stmt_.parameters.get(i);
				virtualReg rs;
				if (paraEntity instanceof register) rs = registerMapping((register) paraEntity);
				else {
					rs = new virtualReg(++ virtualRegCounter);
					if (paraEntity instanceof integerConstant)
						currentBlock.addInst(new liInst(rs, new intImm(((integerConstant) paraEntity).val)));
					else if (paraEntity instanceof booleanConstant)
						currentBlock.addInst(new liInst(rs, new intImm(((booleanConstant) paraEntity).val)));
					else currentBlock.addInst(new mvInst(rs, zero));
				}
				if (i > 7) {
					currentBlock.addInst(new ITypeInst(ITypeInst.opType.addi, sp, sp, new intImm(-4)));
					currentBlock.addInst(new storeInst(storeInst.storeType.sw, rs, new intImm(0), sp));
				} else currentBlock.addInst(new mvInst(physicalReg.pRegs.get("a" + i), rs));
			}
			currentBlock.addInst(new callInst(programAsmEntry.asmFunctions.get(stmt_.callee)));
			// load caller save register
			physicalReg.callerSavePRegs.forEach((name, phyReg) -> currentBlock.addInst(new mvInst(phyReg, virRegs.get(name))));
		} else if (stmt instanceof getelementptr) {
			getelementptr stmt_ = (getelementptr) stmt;
			reg rd = registerMapping((register) stmt_.dest);
			//0. index addressing
			reg rs1;
			if (stmt_.pointer instanceof globalVariable) {
				rs1 = new virtualReg(++ virtualRegCounter);
				currentBlock.addInst(new laInst(rs1, programAsmEntry.gblMapping.get(stmt_.pointer)));
			} else rs1 = registerMapping((register) stmt_.pointer);
			entity idx = stmt_.idxes.get(0);
			if (idx instanceof register) {
				reg offset = new virtualReg(++ virtualRegCounter);
				currentBlock.addInst(new liInst(offset, new intImm(((LLVMPointerType) stmt_.pointer.type).pointeeType.size() / 8)));
				currentBlock.addInst(new RTypeInst(RTypeInst.opType.mul, offset, registerMapping((register) idx), offset));
				currentBlock.addInst(new RTypeInst(RTypeInst.opType.add, rd, rs1, offset));
			} else {
				assert idx instanceof integerConstant;
				currentBlock.addInst(new ITypeInst(ITypeInst.opType.addi, rd, rs1,
					new intImm(((LLVMPointerType) stmt_.pointer.type).pointeeType.size() * ((integerConstant) idx).val)
				));
			}
			//1. member accessing
			if (stmt_.idxes.size() > 1) {
				idx = stmt_.idxes.get(1);
				assert idx instanceof integerConstant;
				assert ((LLVMPointerType) stmt_.pointer.type).pointeeType instanceof LLVMStructureType;
				LLVMStructureType structType = (LLVMStructureType) ((LLVMPointerType) stmt_.pointer.type).pointeeType;
				int offset_ = 0;
				for (int i = 0;i < ((integerConstant) idx).val;++ i)
					offset_ += structType.types.get(i).size() / 8;
				currentBlock.addInst(new ITypeInst(ITypeInst.opType.addi, rd, rd, new intImm(offset_)));
			}
		} else if (stmt instanceof icmp) {
			icmp stmt_ = (icmp) stmt;
			entity lhs = stmt_.op1, rhs = stmt_.op2;
			reg rd = registerMapping((register) stmt_.dest);
			icmp.condCode cond = stmt_.cond;
			if (rhs instanceof register) {
				entity tmp = lhs;
				lhs = rhs;
				rhs = tmp;
				switch (cond) {
					case ugt -> cond = icmp.condCode.ult;
					case uge -> cond = icmp.condCode.ule;
					case ult -> cond = icmp.condCode.ugt;
					case ule -> cond = icmp.condCode.uge;
					case sgt -> cond = icmp.condCode.slt;
					case sge -> cond = icmp.condCode.sle;
					case slt -> cond = icmp.condCode.sgt;
					case sle -> cond = icmp.condCode.sge;
				}
			}
			if (lhs instanceof register) {
				reg lhs_ = registerMapping((register) lhs);
				if (rhs instanceof register) {
					reg rhs_ = registerMapping((register) rhs);
					switch (cond) {
						case eq -> {
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.xor, rd, lhs_, rhs_));
							currentBlock.addInst(new szInst(szInst.opType.seqz, rd, rd));
						}
						case ne -> {
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.xor, rd, lhs_, rhs_));
							currentBlock.addInst(new szInst(szInst.opType.snez, rd, rd));
						}
						case ugt -> currentBlock.addInst(new RTypeInst(RTypeInst.opType.sltu, rd, rhs_, lhs_));
						case uge -> {
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.sltu, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case ult -> currentBlock.addInst(new RTypeInst(RTypeInst.opType.sltu, rd, lhs_, rhs_));
						case ule -> {
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.sltu, rd, rhs_, lhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case sgt -> currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, rhs_, lhs_));
						case sge -> {
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case slt -> currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, lhs_, rhs_));
						case sle -> {
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, rhs_, lhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
					}
				} else {
					assert rhs instanceof integerConstant;
					intImm rhs_ = new intImm(((integerConstant) rhs).val);
					switch (cond) {
						case eq -> {
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, lhs_, rhs_));
							currentBlock.addInst(new szInst(szInst.opType.seqz, rd, rd));
						}
						case ne -> {
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, lhs_, rhs_));
							currentBlock.addInst(new szInst(szInst.opType.snez, rd, rd));
						}
						case ugt -> {
							reg rhs__ = new virtualReg(++ virtualRegCounter);
							currentBlock.addInst(new liInst(rhs__, rhs_));
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.sltu, rd, rhs__, lhs_));
						}
						case uge -> {
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.sltiu, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case ult -> currentBlock.addInst(new ITypeInst(ITypeInst.opType.sltiu, rd, lhs_, rhs_));
						case ule -> {
							reg rhs__ = new virtualReg(++ virtualRegCounter);
							currentBlock.addInst(new liInst(rhs__, rhs_));
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.sltu, rd, rhs__, lhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case sgt -> {
							reg rhs__ = new virtualReg(++ virtualRegCounter);
							currentBlock.addInst(new liInst(rhs__, rhs_));
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, rhs__, lhs_));
						}
						case sge -> {
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.slti, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case slt -> currentBlock.addInst(new ITypeInst(ITypeInst.opType.slti, rd, lhs_, rhs_));
						case sle -> {
							reg rhs__ = new virtualReg(++ virtualRegCounter);
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, rhs__, lhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
					}
				}
			} else {
				assert lhs instanceof integerConstant && rhs instanceof integerConstant;
				int lhs_ = ((integerConstant) lhs).val, rhs_ = ((integerConstant) rhs).val;
				switch (cond) {
					case eq -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ == rhs_ ? 1 : 0)));
					case ne -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ != rhs_ ? 1 : 0)));
					case ugt -> currentBlock.addInst(new liInst(rd, new intImm(compareUnsigned(lhs_, rhs_) > 0 ? 1 : 0)));
					case uge -> currentBlock.addInst(new liInst(rd, new intImm(compareUnsigned(lhs_, rhs_) >= 0 ? 1 : 0)));
					case ult -> currentBlock.addInst(new liInst(rd, new intImm(compareUnsigned(lhs_, rhs_) < 0 ? 1 : 0)));
					case ule -> currentBlock.addInst(new liInst(rd, new intImm(compareUnsigned(lhs_, rhs_) <= 0 ? 1 : 0)));
					case sgt -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ > rhs_ ? 1 : 0)));
					case sge -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ >= rhs_ ? 1 : 0)));
					case slt -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ < rhs_ ? 1 : 0)));
					case sle -> currentBlock.addInst(new liInst(rd, new intImm(lhs_ <= rhs_ ? 1 : 0)));
				}
			}

		} else if (stmt instanceof load) {
			load stmt_ = (load) stmt;
			reg rd = registerMapping((register) stmt_.dest);
			if (stmt_.pointer instanceof globalVariable) {
				globalData glb = programAsmEntry.gblMapping.get(stmt_.pointer);
				currentBlock.addInst(new luiInst(rd, new relocationImm(relocationImm.type.hi, glb)));
				currentBlock.addInst(new loadInst(loadInst.loadType.lw, rd, rd, new relocationImm(relocationImm.type.lo, glb)));
			} else {
				currentBlock.addInst(new loadInst(
					((LLVMPointerType) stmt_.pointer.type).pointeeType.size() == 8 ? loadInst.loadType.lbu : loadInst.loadType.lw,
					rd, registerMapping((register) stmt_.pointer), new intImm(0)
				));
			}
		} else if (stmt instanceof ret) {
			ret stmt_ = (ret) stmt;
			if (stmt_.value != null)
				if (stmt_.value instanceof booleanConstant)
					currentBlock.addInst(new luiInst(a0, new intImm(((booleanConstant) stmt_.value).val)));
				else if (stmt_.value instanceof integerConstant)
					currentBlock.addInst(new luiInst(a0, new intImm(((integerConstant) stmt_.value).val)));
				else if (stmt_.value instanceof nullPointerConstant)
					currentBlock.addInst(new mvInst(a0, zero));
				else {
					assert stmt_.value instanceof register;
					currentBlock.addInst(new mvInst(a0, registerMapping((register) stmt_.value)));
				}
			currentBlock.addInst(new jumpInst(currentFunction.retBlock));
		} else {
			assert stmt instanceof store;
			store stmt_ = (store) stmt;
			reg rs2;
			if (stmt_.value instanceof register) rs2 = registerMapping((register) stmt_.value);
			else {
				rs2 = new virtualReg(++ virtualRegCounter);
				if (stmt_.value instanceof booleanConstant)
					currentBlock.addInst(new liInst(rs2, new intImm(((booleanConstant) stmt_.value).val)));
				else if (stmt_.value instanceof integerConstant)
					currentBlock.addInst(new liInst(rs2, new intImm(((integerConstant) stmt_.value).val)));
				else currentBlock.addInst(new mvInst(rs2, zero));
			}
		if (stmt_.value instanceof integerConstant)
			if (stmt_.pointer instanceof globalVariable) {
				globalData glb = programAsmEntry.gblMapping.get(stmt_.pointer);
				reg rs1 = new virtualReg(++ virtualRegCounter);
				currentBlock.addInst(new luiInst(rs1, new relocationImm(relocationImm.type.hi, glb)));
				currentBlock.addInst(new storeInst(storeInst.storeType.sw, rs2,
					new relocationImm(relocationImm.type.lo, glb), rs1));
			} else
				currentBlock.addInst(new storeInst(
					stmt_.value.type.size() == 8 ? storeInst.storeType.sb : storeInst.storeType.sw,
					registerMapping((register) stmt_.pointer), new intImm(0), rs2
				));
		}
	}

	private void buildAsmBlock(basicBlock block) {
		asmBlock asmBlk = blockMapping(block);
		asmBlk.comment = block.name;
		currentFunction.asmBlocks.add(asmBlk);
		currentBlock = asmBlk;
		block.stmts.forEach(this::buildAsmInst);
		currentBlock = null;
	}

	private void buildAsmFunction(function func) {
		stackFrame stkFrame = new stackFrame();
		ArrayList<virtualReg> paraVRegs = new ArrayList<>();
		func.argValues.forEach(arg -> paraVRegs.add(registerMapping((register) arg)));
		asmFunction asmFunc = new asmFunction(func.functionName, stkFrame, paraVRegs);
		asmFunc.initBlock = new asmBlock(++ blockCounter);
		asmFunc.initBlock.comment = "init block of " + func.functionName;
		asmFunc.retBlock = new asmBlock(++ blockCounter);
		asmFunc.retBlock.comment = "return block of " + func.functionName;
		programAsmEntry.asmFunctions.put(func, asmFunc);
		currentFunction = asmFunc;
		func.blocks.forEach(this::buildAsmBlock);
		currentFunction = null;
	}

	private void registerGlobalVariable() {
		for (globalVariable global: programIREntry.globals)
			if (global.isString)
				programAsmEntry.gblMapping.put(global, new globalData(global.name, global.val, global.isConstant));
			else
				programAsmEntry.gblMapping.put(global, new globalData(global.name, Integer.parseInt(global.val), global.isConstant));
	}

	@Override
	public void run() {
		registerGlobalVariable();
		programIREntry.functions.forEach(this::buildAsmFunction);
	}
}
