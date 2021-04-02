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
import LLVMIR.TypeSystem.LLVMArrayType;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMStructureType;
import LLVMIR.basicBlock;
import LLVMIR.Instruction.*;
import LLVMIR.function;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.compareUnsigned;

public class instructionSelector implements pass {
	private final IREntry programIREntry;
	private final asmEntry programAsmEntry;
	private int blockCounter = 0;
	private int virtualRegCounter = 0;

	private asmFunction currentFunction;
	private asmBlock currentBlock;

	private final HashMap<register, virtualReg> regMap = new HashMap<>();
	private final HashMap<basicBlock, asmBlock> blkMap = new HashMap<>();

	static virtualReg sp = physicalReg.pRegToVReg.get(physicalReg.pRegs.get("sp"));
	static virtualReg a0 = physicalReg.pRegToVReg.get(physicalReg.pRegs.get("a0"));
	static virtualReg zero = physicalReg.pRegToVReg.get(physicalReg.pRegs.get("zero"));
	static virtualReg ra = physicalReg.pRegToVReg.get(physicalReg.pRegs.get("ra"));

	private virtualReg registerMapping(register reg) {
		if (!regMap.containsKey(reg)) regMap.put(reg, createNewVirtualRegister());
		return regMap.get(reg);
	}

	private asmBlock blockMapping(basicBlock blk) {
		if (!blkMap.containsKey(blk)) blkMap.put(blk, new asmBlock(++ blockCounter, blk.loopDepth));
		return blkMap.get(blk);
	}

	public instructionSelector(IREntry programIREntry, asmEntry programAsmEntry) {
		this.programIREntry = programIREntry;
		this.programAsmEntry = programAsmEntry;
	}

	private virtualReg createNewVirtualRegister() {
		virtualReg vReg = new virtualReg(++ virtualRegCounter);
		currentFunction.virtualRegs.add(vReg);
		return vReg;
	}

	private void buildAsmInst(statement stmt) {
		if (stmt instanceof _move) {
			_move stmt_ = (_move) stmt;
			virtualReg rd = registerMapping((register) stmt_.dest);
			if (stmt_.src instanceof integerConstant) currentBlock.addInst(new liInst(currentBlock, rd, new intImm(((integerConstant) stmt_.src).val)));
			else if (stmt_.src instanceof booleanConstant) currentBlock.addInst(new liInst(currentBlock, rd, new intImm(((booleanConstant) stmt_.src).val)));
			else if (stmt_.src instanceof nullPointerConstant) currentBlock.addInst(new liInst(currentBlock, rd, new intImm(0)));
			else if (stmt_.src instanceof register) currentBlock.addInst(new mvInst(currentBlock, rd, registerMapping((register) stmt_.src)));
			else if (!(stmt_.src instanceof undefinedValue)) {
				assert stmt_.src instanceof globalVariable;
				globalData glb = programAsmEntry.gblMapping.get(stmt_.src);
				currentBlock.addInst(new luiInst(currentBlock, rd, new relocationImm(relocationImm.type.hi, glb)));
				currentBlock.addInst(new loadInst(currentBlock, loadInst.loadType.lw, rd, rd, new relocationImm(relocationImm.type.lo, glb)));
			}
		} else if (stmt instanceof binary) {
			binary stmt_ = (binary) stmt;
			entity lhs = stmt_.op1, rhs = stmt_.op2;
			virtualReg rd = registerMapping((register) stmt_.dest);
			binary.instCode inst = stmt_.inst;
			if (inst == binary.instCode.sub || inst == binary.instCode.sdiv || inst == binary.instCode.srem ||
				inst == binary.instCode.shl || inst == binary.instCode.ashr || inst == binary.instCode.mul) {
				virtualReg rs1, rs2;
				if (lhs instanceof register) rs1 = registerMapping((register) lhs);
				else {
					assert lhs instanceof integerConstant || lhs instanceof booleanConstant;
					int val = lhs instanceof integerConstant ? ((integerConstant) lhs).val : ((booleanConstant) lhs).val;
					rs1 = createNewVirtualRegister();
					currentBlock.addInst(new liInst(currentBlock, rs1, new intImm(val)));
				}
				if (rhs instanceof register) rs2 = registerMapping((register) rhs);
				else {
					assert rhs instanceof integerConstant || rhs instanceof booleanConstant;
					int val = rhs instanceof integerConstant ? ((integerConstant) rhs).val : ((booleanConstant) rhs).val;
					rs2 = createNewVirtualRegister();
					currentBlock.addInst(new liInst(currentBlock, rs2, new intImm(val)));
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
				currentBlock.addInst(new RTypeInst(currentBlock, type, rd, rs1, rs2));
			} else {
				if (rhs instanceof register) {
					entity tmp = lhs;
					lhs = rhs;
					rhs = tmp;
				}
				if (lhs instanceof register) {
					virtualReg lhs_ = registerMapping((register) lhs);
					if (rhs instanceof register) {
						virtualReg rhs_ = registerMapping((register) rhs);
						RTypeInst.opType type = null;
						switch (inst) {
							case or -> type = RTypeInst.opType.or;
							case add -> type = RTypeInst.opType.add;
							case and -> type = RTypeInst.opType.and;
							case xor -> type = RTypeInst.opType.xor;
						}
						currentBlock.addInst(new RTypeInst(currentBlock, type, rd, lhs_, rhs_));
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
						currentBlock.addInst(new ITypeInst(currentBlock, type, rd, lhs_, intImm));
					}
				} else {
					assert (lhs instanceof integerConstant || lhs instanceof booleanConstant) &&
						(rhs instanceof integerConstant || rhs instanceof booleanConstant);
					int lhs_ = lhs instanceof integerConstant ? ((integerConstant) lhs).val : ((booleanConstant) lhs).val;
					int rhs_ = rhs instanceof integerConstant ? ((integerConstant) rhs).val : ((booleanConstant) rhs).val;
					switch (inst) {
						case add -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ + rhs_)));
						case and -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ & rhs_)));
						case or -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ | rhs_)));
						case xor -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ ^ rhs_)));
					}
				}
			}
		} else if (stmt instanceof bitcast) {
			bitcast stmt_ = (bitcast) stmt;
			virtualReg rd = registerMapping((register) stmt_.dest);
			if (stmt_.value instanceof globalVariable) currentBlock.addInst(new laInst(currentBlock, rd, programAsmEntry.gblMapping.get(stmt_.value)));
			else currentBlock.addInst(new mvInst(currentBlock, rd, registerMapping((register) stmt_.value)));
		} else if (stmt instanceof br) {
			br stmt_ = (br) stmt;
			asmBlock trueBlk = blkMap.get(stmt_.trueBranch);
			if (stmt_.cond == null) currentBlock.addInst(new jumpInst(currentBlock, trueBlk));
			else {
				asmBlock falseBlk = blkMap.get(stmt_.falseBranch);
				if (stmt_.cond instanceof booleanConstant)
					if (((booleanConstant) stmt_.cond).val == 1) currentBlock.addInst(new jumpInst(currentBlock, trueBlk));
					else currentBlock.addInst(new jumpInst(currentBlock, falseBlk));
				else {
					virtualReg cond = registerMapping((register) stmt_.cond);
					boolean merged = false;
					inst tailInst = currentBlock.tailInst;
					if (tailInst != null)
						if (tailInst instanceof ITypeInst && tailInst.pre instanceof RTypeInst) {
							RTypeInst preInst = (RTypeInst) tailInst.pre;
							if (((ITypeInst) tailInst).testMergeability(cond) && preInst.testMergeability(cond)) {
								if (preInst.type == RTypeInst.opType.slt) currentBlock.tailInst = new brInst(currentBlock, brInst.opType.blt, preInst.rs1, preInst.rs2, falseBlk);
								else currentBlock.tailInst = new brInst(currentBlock, brInst.opType.bltu, preInst.rs2, preInst.rs1, falseBlk);
								currentBlock.tailInst.pre = preInst.pre;
								if (preInst.pre != null) preInst.pre.suf = currentBlock.tailInst;
								else currentBlock.headInst = currentBlock.tailInst;
								preInst.removeFromBlockWithoutRelinking();
								tailInst.removeFromBlockWithoutRelinking();
								merged = true;
							}
						} else {
							inst preInst = tailInst.pre;
							if (tailInst instanceof szInst) {
								if (((szInst) tailInst).testMergeability(cond)) {
									currentBlock.tailInst = new brInst(currentBlock,
										((szInst) tailInst).type == szInst.opType.seqz ? brInst.opType.bnez : brInst.opType.beqz,
										tailInst.rs1, null, falseBlk
									);
									merged = true;
								}
							} else if (tailInst instanceof RTypeInst) {
								if (((RTypeInst) tailInst).testMergeability(cond)) {
									if (((RTypeInst) tailInst).type == RTypeInst.opType.slt)
										currentBlock.tailInst = new brInst(currentBlock, brInst.opType.bge, tailInst.rs1, tailInst.rs2, falseBlk);
									else
										currentBlock.tailInst = new brInst(currentBlock, brInst.opType.bgeu, tailInst.rs1, tailInst.rs2, falseBlk);
									merged = true;
								}
							}
							if (merged) {
								if (preInst == null) currentBlock.headInst = currentBlock.tailInst;
								else (currentBlock.tailInst.pre = preInst).suf = currentBlock.tailInst;
								tailInst.removeFromBlockWithoutRelinking();
							}
						}
					if (!merged) currentBlock.addInst(new brInst(currentBlock, brInst.opType.beqz, cond, null, falseBlk));
				}
			}
		} else if (stmt instanceof call) {
			call stmt_ = (call) stmt;
			ArrayList<intImm> argOffsets = new ArrayList<>();
			for (int i = 0;i < stmt_.parameters.size();++ i) {
				entity paraEntity = stmt_.parameters.get(i);
				virtualReg rs;
				if (paraEntity instanceof register) rs = registerMapping((register) paraEntity);
				else {
					rs = createNewVirtualRegister();
					if (paraEntity instanceof integerConstant)
						currentBlock.addInst(new liInst(currentBlock, rs, new intImm(((integerConstant) paraEntity).val)));
					else if (paraEntity instanceof booleanConstant)
						currentBlock.addInst(new liInst(currentBlock, rs, new intImm(((booleanConstant) paraEntity).val)));
					else currentBlock.addInst(new mvInst(currentBlock, rs, zero));
				}
				if (i > 7) {
					intImm offset = new intImm();
					currentBlock.addInst(new storeInst(currentBlock, storeInst.storeType.sw, rs, offset, sp));
					argOffsets.add(offset);
				} else currentBlock.addInst(new mvInst(currentBlock, physicalReg.pRegToVReg.get(physicalReg.pRegs.get("a" + i)), rs));
			}
			currentFunction.stkFrame.calleeParameterOffsets.put(stmt_.callee, argOffsets);
			currentBlock.addInst(new callInst(currentBlock, programAsmEntry.asmFunctions.get(stmt_.callee)));
			if (stmt_.dest != null) currentBlock.addInst(new mvInst(currentBlock, registerMapping((register) stmt_.dest), a0));
		} else if (stmt instanceof getelementptr) {
			getelementptr stmt_ = (getelementptr) stmt;
			virtualReg rd = registerMapping((register) stmt_.dest);
			//0. index addressing
			virtualReg rs1;
			if (stmt_.pointer instanceof globalVariable) {
				rs1 = createNewVirtualRegister();
				currentBlock.addInst(new laInst(currentBlock, rs1, programAsmEntry.gblMapping.get(stmt_.pointer)));
			} else rs1 = registerMapping((register) stmt_.pointer);
			entity idx = stmt_.idxes.get(0);
			if (idx instanceof register) {
				virtualReg offset = createNewVirtualRegister();
				currentBlock.addInst(new liInst(currentBlock, offset, new intImm(((LLVMPointerType) stmt_.pointer.type).pointeeType.size() / 8)));
				currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.mul, offset, registerMapping((register) idx), offset));
				currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.add, rd, rs1, offset));
			} else {
				assert idx instanceof integerConstant;
				currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.addi, rd, rs1,
					new intImm(((LLVMPointerType) stmt_.pointer.type).pointeeType.size() / 8 * ((integerConstant) idx).val)
				));
			}
			//1. member accessing
			if (stmt_.idxes.size() > 1) {
				assert idx instanceof integerConstant;
				idx = stmt_.idxes.get(1);
				LLVMPointerType ptr = (LLVMPointerType) stmt_.pointer.type;
				if (ptr.pointeeType instanceof LLVMStructureType) {
					LLVMStructureType structType = (LLVMStructureType) ptr.pointeeType;
					int offset_ = 0;
					for (int i = 0; i < ((integerConstant) idx).val; ++i)
						offset_ += structType.types.get(i).size() / 8;
					currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.addi, rd, rd, new intImm(offset_)));
				} else {
					assert ptr.pointeeType instanceof LLVMArrayType;
					currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.addi, rd, rd, new intImm(((integerConstant) idx).val)));
				}
			}
		} else if (stmt instanceof icmp) {
			icmp stmt_ = (icmp) stmt;
			entity lhs = stmt_.op1, rhs = stmt_.op2;
			virtualReg rd = registerMapping((register) stmt_.dest);
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
				virtualReg lhs_ = registerMapping((register) lhs);
				if (rhs instanceof register) {
					virtualReg rhs_ = registerMapping((register) rhs);
					switch (cond) {
						case eq -> {
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.xor, rd, lhs_, rhs_));
							currentBlock.addInst(new szInst(currentBlock, szInst.opType.seqz, rd, rd));
						}
						case ne -> {
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.xor, rd, lhs_, rhs_));
							currentBlock.addInst(new szInst(currentBlock, szInst.opType.snez, rd, rd));
						}
						case ugt -> currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.sltu, rd, rhs_, lhs_));
						case uge -> {
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.sltu, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case ult -> currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.sltu, rd, lhs_, rhs_));
						case ule -> {
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.sltu, rd, rhs_, lhs_));
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case sgt -> currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.slt, rd, rhs_, lhs_));
						case sge -> {
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.slt, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case slt -> currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.slt, rd, lhs_, rhs_));
						case sle -> {
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.slt, rd, rhs_, lhs_));
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
					}
				} else {
					assert rhs instanceof integerConstant;
					intImm rhs_ = new intImm(((integerConstant) rhs).val);
					switch (cond) {
						case eq -> {
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, lhs_, rhs_));
							currentBlock.addInst(new szInst(currentBlock, szInst.opType.seqz, rd, rd));
						}
						case ne -> {
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, lhs_, rhs_));
							currentBlock.addInst(new szInst(currentBlock, szInst.opType.snez, rd, rd));
						}
						case ugt -> {
							virtualReg rhs__ = createNewVirtualRegister();
							currentBlock.addInst(new liInst(currentBlock, rhs__, rhs_));
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.sltu, rd, rhs__, lhs_));
						}
						case uge -> {
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.sltiu, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case ult -> currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.sltiu, rd, lhs_, rhs_));
						case ule -> {
							virtualReg rhs__ = createNewVirtualRegister();
							currentBlock.addInst(new liInst(currentBlock, rhs__, rhs_));
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.sltu, rd, rhs__, lhs_));
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case sgt -> {
							virtualReg rhs__ = createNewVirtualRegister();
							currentBlock.addInst(new liInst(currentBlock, rhs__, rhs_));
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.slt, rd, rhs__, lhs_));
						}
						case sge -> {
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.slti, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
						case slt -> currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.slti, rd, lhs_, rhs_));
						case sle -> {
							virtualReg rhs__ = createNewVirtualRegister();
							currentBlock.addInst(new RTypeInst(currentBlock, RTypeInst.opType.slt, rd, rhs__, lhs_));
							currentBlock.addInst(new ITypeInst(currentBlock, ITypeInst.opType.xori, rd, rd, new intImm(1)));
						}
					}
				}
			} else {
				assert lhs instanceof integerConstant && rhs instanceof integerConstant;
				int lhs_ = ((integerConstant) lhs).val, rhs_ = ((integerConstant) rhs).val;
				switch (cond) {
					case eq -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ == rhs_ ? 1 : 0)));
					case ne -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ != rhs_ ? 1 : 0)));
					case ugt -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(compareUnsigned(lhs_, rhs_) > 0 ? 1 : 0)));
					case uge -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(compareUnsigned(lhs_, rhs_) >= 0 ? 1 : 0)));
					case ult -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(compareUnsigned(lhs_, rhs_) < 0 ? 1 : 0)));
					case ule -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(compareUnsigned(lhs_, rhs_) <= 0 ? 1 : 0)));
					case sgt -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ > rhs_ ? 1 : 0)));
					case sge -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ >= rhs_ ? 1 : 0)));
					case slt -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ < rhs_ ? 1 : 0)));
					case sle -> currentBlock.addInst(new liInst(currentBlock, rd, new intImm(lhs_ <= rhs_ ? 1 : 0)));
				}
			}

		} else if (stmt instanceof load) {
			load stmt_ = (load) stmt;
			virtualReg rd = registerMapping((register) stmt_.dest);
			if (stmt_.pointer instanceof globalVariable) {
				globalData glb = programAsmEntry.gblMapping.get(stmt_.pointer);
				currentBlock.addInst(new luiInst(currentBlock, rd, new relocationImm(relocationImm.type.hi, glb)));
				currentBlock.addInst(new loadInst(currentBlock, loadInst.loadType.lw, rd, rd, new relocationImm(relocationImm.type.lo, glb)));
			} else {
				currentBlock.addInst(new loadInst(currentBlock, 
					((LLVMPointerType) stmt_.pointer.type).pointeeType.size() == 8 ? loadInst.loadType.lbu : loadInst.loadType.lw,
					rd, registerMapping((register) stmt_.pointer), new intImm(0)
				));
			}
		} else if (stmt instanceof ret) {
			ret stmt_ = (ret) stmt;
			if (stmt_.value != null)
				if (stmt_.value instanceof booleanConstant)
					currentBlock.addInst(new luiInst(currentBlock, a0, new intImm(((booleanConstant) stmt_.value).val)));
				else if (stmt_.value instanceof integerConstant)
					currentBlock.addInst(new luiInst(currentBlock, a0, new intImm(((integerConstant) stmt_.value).val)));
				else if (stmt_.value instanceof nullPointerConstant)
					currentBlock.addInst(new mvInst(currentBlock, a0, zero));
				else {
					assert stmt_.value instanceof register;
					currentBlock.addInst(new mvInst(currentBlock, a0, registerMapping((register) stmt_.value)));
				}
			currentBlock.addInst(new jumpInst(currentBlock, currentFunction.retBlock));
			currentBlock.addSuccessor(currentFunction.retBlock);
		} else {
			assert stmt instanceof store;
			store stmt_ = (store) stmt;
			virtualReg rs2;
			if (stmt_.value instanceof register) rs2 = registerMapping((register) stmt_.value);
			else {
				rs2 = createNewVirtualRegister();
				if (stmt_.value instanceof booleanConstant)
					currentBlock.addInst(new liInst(currentBlock, rs2, new intImm(((booleanConstant) stmt_.value).val)));
				else if (stmt_.value instanceof integerConstant)
					currentBlock.addInst(new liInst(currentBlock, rs2, new intImm(((integerConstant) stmt_.value).val)));
				else currentBlock.addInst(new mvInst(currentBlock, rs2, zero));
			}
			if (stmt_.pointer instanceof globalVariable) {
				globalData glb = programAsmEntry.gblMapping.get(stmt_.pointer);
				virtualReg rs1 = createNewVirtualRegister();
				currentBlock.addInst(new luiInst(currentBlock, rs1, new relocationImm(relocationImm.type.hi, glb)));
				currentBlock.addInst(new storeInst(currentBlock, storeInst.storeType.sw, rs2,
					new relocationImm(relocationImm.type.lo, glb), rs1));
			} else
				currentBlock.addInst(new storeInst(currentBlock,
					stmt_.value.type.size() == 8 ? storeInst.storeType.sb : storeInst.storeType.sw,
					rs2, new intImm(0), registerMapping((register) stmt_.pointer)
				));
		}
	}

	private void buildAsmBlock(basicBlock block) {
		asmBlock asmBlk = blockMapping(block);
		asmBlk.comment = block.name;
		block.successors().forEach(blk -> asmBlk.addSuccessor(blockMapping(blk)));
		currentBlock = asmBlk;
		block.stmts.forEach(this::buildAsmInst);
		currentBlock = null;
	}

	private void buildAsmFunction(function func) {
		asmFunction asmFunc = programAsmEntry.asmFunctions.get(func);
		currentFunction = asmFunc;
		func.argValues.forEach(arg -> asmFunc.parameters.add(registerMapping((register) arg)));
		asmFunc.stkFrame = new stackFrame(asmFunc);
		asmFunc.initBlock = new asmBlock(++ blockCounter, 0);
		asmFunc.initBlock.comment = "init block of " + func.functionName;
		currentBlock = asmFunc.initBlock;
		virtualReg returnAddress = createNewVirtualRegister();
		asmFunc.initBlock.addInst(new mvInst(currentBlock, returnAddress, ra));
		ArrayList<intImm> argOffsets = new ArrayList<>();
		for (int i = 0;i < func.argValues.size();++ i) {
			virtualReg vReg = registerMapping((register) func.argValues.get(i));
			if (i > 7) {
				intImm offset = new intImm();
				argOffsets.add(offset);
				asmFunc.initBlock.addInst(new loadInst(currentBlock, loadInst.loadType.lw, vReg, sp, offset));
			} else asmFunc.initBlock.addInst(new mvInst(currentBlock, vReg, physicalReg.pRegToVReg.get(physicalReg.pRegs.get("a" + i))));
		}
		asmFunc.stkFrame.callerParameterOffsets = argOffsets;
		ArrayList<virtualReg> calleeSavers = new ArrayList<>();
		for (int i = 0;i < 12;++ i) {
			virtualReg calleeSaver = createNewVirtualRegister();
			calleeSavers.add(calleeSaver);
			asmFunc.initBlock.addInst(new mvInst(currentBlock, calleeSaver, physicalReg.pRegToVReg.get(physicalReg.pRegs.get("s" + i))));
		}
		currentBlock = null;
		func.blocks.forEach(blk -> currentFunction.asmBlocks.add(blockMapping(blk)));
		asmFunc.retBlock = new asmBlock(++ blockCounter, 0);
		asmFunc.retBlock.comment = "return block of " + func.functionName;
		currentBlock = asmFunc.retBlock;
		for (int i = 0;i < 12;++ i)
			asmFunc.retBlock.addInst(new mvInst(currentBlock, physicalReg.pRegToVReg.get(physicalReg.pRegs.get("s" + i)), calleeSavers.get(i)));
		asmFunc.retBlock.addInst(new mvInst(currentBlock, ra, returnAddress));
		asmFunc.retBlock.addInst(new retInst(currentBlock));
		currentBlock = null;
		func.blocks.forEach(this::buildAsmBlock);
		asmFunc.initBlock.addSuccessor(asmFunc.asmBlocks.get(0));
		currentFunction = null;
	}

	private void registerGlobalVariable() {
		for (globalVariable global: programIREntry.globals)
			if (global.isString)
				programAsmEntry.gblMapping.put(global, new globalData(global.name, global.val));
			else if (global.val.equals("null"))
				programAsmEntry.gblMapping.put(global, new globalData(global.name, 0));
			else
				programAsmEntry.gblMapping.put(global, new globalData(global.name, Integer.parseInt(global.val)));
	}

	@Override
	public void run() {
		registerGlobalVariable();
		programIREntry.functions.forEach(IRFunc -> programAsmEntry.asmFunctions.put(IRFunc, new asmFunction(IRFunc.functionName, IRFunc.blocks == null ? null : new ArrayList<>())));
		programIREntry.functions.stream().filter(IRFunc -> IRFunc.blocks != null).forEach(this::buildAsmFunction);
	}
}
