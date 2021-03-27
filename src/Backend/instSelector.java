package Backend;

import Assembly.Instruction.*;
import Assembly.Operand.Imm;
import Assembly.Operand.reg;
import Assembly.Operand.virtualReg;
import Assembly.asmBlock;
import Assembly.asmEntry;
import Assembly.asmFunction;
import LLVMIR.IREntry;
import LLVMIR.Instruction.statement;
import LLVMIR.Operand.*;
import LLVMIR.TypeSystem.LLVMPointerType;
import LLVMIR.TypeSystem.LLVMStructureType;
import LLVMIR.basicBlock;
import LLVMIR.Instruction.*;
import LLVMIR.function;

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

	private virtualReg registerMapping(register reg) {
		if (!regMap.containsKey(reg)) regMap.put(reg, new virtualReg(++ virtualRegCounter));
		return regMap.get(reg);
	}

	public instSelector(IREntry programIREntry, asmEntry programAsmEntry) {
		this.programIREntry = programIREntry;
		this.programAsmEntry = programAsmEntry;
	}

	private void buildAsmInst(statement stmt) {
		if (stmt instanceof _move) {
			_move stmt_ = (_move) stmt;
			reg rd = registerMapping((register) stmt_.dest);
			if (stmt_.src instanceof globalVariable) {
				// TODO: 2021/3/27 deal with data segmentation
			} else if (stmt_.src instanceof integerConstant) currentBlock.addInst(new liInst(rd, new Imm(((integerConstant) stmt_.src).val)));
			else if (stmt_.src instanceof booleanConstant) currentBlock.addInst(new liInst(rd, new Imm(((booleanConstant) stmt_.src).val)));
			else if (stmt_.src instanceof nullPointerConstant) currentBlock.addInst(new liInst(rd, new Imm(0)));
			else currentBlock.addInst(new mvInst(rd, registerMapping((register) stmt_.src)));
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
					currentBlock.addInst(new liInst(rs1, new Imm(val)));
				}
				if (rhs instanceof register) rs2 = registerMapping((register) rhs);
				else {
					assert rhs instanceof integerConstant || rhs instanceof booleanConstant;
					int val = rhs instanceof integerConstant ? ((integerConstant) rhs).val : ((booleanConstant) rhs).val;
					rs2 = new virtualReg(++ virtualRegCounter);
					currentBlock.addInst(new liInst(rs2, new Imm(val)));
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
						Imm imm = new Imm(rhs instanceof integerConstant ? ((integerConstant) rhs).val : ((booleanConstant) rhs).val);
						ITypeInst.opType type = null;
						switch (inst) {
							case or -> type = ITypeInst.opType.ori;
							case add -> type = ITypeInst.opType.addi;
							case and -> type = ITypeInst.opType.andi;
							case xor -> type = ITypeInst.opType.xori;
						}
						currentBlock.addInst(new ITypeInst(type, rd, lhs_, imm));
					}
				} else {
					assert (lhs instanceof integerConstant || lhs instanceof booleanConstant) &&
						(rhs instanceof integerConstant || rhs instanceof booleanConstant);
					int lhs_ = lhs instanceof integerConstant ? ((integerConstant) lhs).val : ((booleanConstant) lhs).val;
					int rhs_ = rhs instanceof integerConstant ? ((integerConstant) rhs).val : ((booleanConstant) rhs).val;
					switch (inst) {
						case add -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ + rhs_)));
						case and -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ & rhs_)));
						case or -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ | rhs_)));
						case xor -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ ^ rhs_)));
					}
				}
			}
		} else if (stmt instanceof bitcast) {
			// TODO: 2021/3/27 deal with data segmentation
			bitcast stmt_ = (bitcast) stmt;
			currentBlock.addInst(new mvInst(registerMapping((register) stmt_.dest),
				registerMapping((register) stmt_.value)
			));
		} else if (stmt instanceof br) {

		} else if (stmt instanceof call) {

		} else if (stmt instanceof getelementptr) {
			getelementptr stmt_ = (getelementptr) stmt;
			reg rd = registerMapping((register) stmt_.dest);
			//0. index addressing
			if (stmt_.pointer instanceof globalVariable) {
				// TODO: 2021/3/27 deal with data segmentation
			} else {
				reg rs1 = registerMapping((register) stmt_.pointer);
				entity idx = stmt_.idxes.get(0);
				if (idx instanceof register) {
					reg offset = new virtualReg(++ virtualRegCounter);
					currentBlock.addInst(new liInst(offset, new Imm(((LLVMPointerType) stmt_.pointer.type).pointeeType.size() / 8)));
					currentBlock.addInst(new RTypeInst(RTypeInst.opType.mul, offset, registerMapping((register) idx), offset));
					currentBlock.addInst(new RTypeInst(RTypeInst.opType.add, rd, rs1, offset));
				} else if (idx instanceof globalVariable) {
					// TODO: 2021/3/27 deal with data segmentation
				} else {
					assert idx instanceof integerConstant;
					currentBlock.addInst(new ITypeInst(ITypeInst.opType.addi, rd, rs1,
						new Imm(((LLVMPointerType) stmt_.pointer.type).pointeeType.size() * ((integerConstant) idx).val)
					));
				}
			}
			//1. member accessing
			if (stmt_.idxes.size() > 1) {
				entity idx = stmt_.idxes.get(1);
				assert idx instanceof integerConstant;
				assert ((LLVMPointerType) stmt_.pointer.type).pointeeType instanceof LLVMStructureType;
				LLVMStructureType structType = (LLVMStructureType) ((LLVMPointerType) stmt_.pointer.type).pointeeType;
				int offset_ = 0;
				for (int i = 0;i < ((integerConstant) idx).val;++ i)
					offset_ += structType.types.get(i).size() / 8;
				currentBlock.addInst(new ITypeInst(ITypeInst.opType.addi, rd, rd, new Imm(offset_)));
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
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new Imm(1)));
						}
						case ult -> currentBlock.addInst(new RTypeInst(RTypeInst.opType.sltu, rd, lhs_, rhs_));
						case ule -> {
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.sltu, rd, rhs_, lhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new Imm(1)));
						}
						case sgt -> currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, rhs_, lhs_));
						case sge -> {
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new Imm(1)));
						}
						case slt -> currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, lhs_, rhs_));
						case sle -> {
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, rhs_, lhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new Imm(1)));
						}
					}
				} else {
					assert rhs instanceof integerConstant;
					Imm rhs_ = new Imm(((integerConstant) rhs).val);
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
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new Imm(1)));
						}
						case ult -> currentBlock.addInst(new ITypeInst(ITypeInst.opType.sltiu, rd, lhs_, rhs_));
						case ule -> {
							reg rhs__ = new virtualReg(++ virtualRegCounter);
							currentBlock.addInst(new liInst(rhs__, rhs_));
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.sltu, rd, rhs__, lhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new Imm(1)));
						}
						case sgt -> {
							reg rhs__ = new virtualReg(++ virtualRegCounter);
							currentBlock.addInst(new liInst(rhs__, rhs_));
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, rhs__, lhs_));
						}
						case sge -> {
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.slti, rd, lhs_, rhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new Imm(1)));
						}
						case slt -> currentBlock.addInst(new ITypeInst(ITypeInst.opType.slti, rd, lhs_, rhs_));
						case sle -> {
							reg rhs__ = new virtualReg(++ virtualRegCounter);
							currentBlock.addInst(new RTypeInst(RTypeInst.opType.slt, rd, rhs__, lhs_));
							currentBlock.addInst(new ITypeInst(ITypeInst.opType.xori, rd, rd, new Imm(1)));
						}
					}
				}
			} else {
				assert lhs instanceof integerConstant && rhs instanceof integerConstant;
				int lhs_ = ((integerConstant) lhs).val, rhs_ = ((integerConstant) rhs).val;
				switch (cond) {
					case eq -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ == rhs_ ? 1 : 0)));
					case ne -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ != rhs_ ? 1 : 0)));
					case ugt -> currentBlock.addInst(new liInst(rd, new Imm(compareUnsigned(lhs_, rhs_) > 0 ? 1 : 0)));
					case uge -> currentBlock.addInst(new liInst(rd, new Imm(compareUnsigned(lhs_, rhs_) >= 0 ? 1 : 0)));
					case ult -> currentBlock.addInst(new liInst(rd, new Imm(compareUnsigned(lhs_, rhs_) < 0 ? 1 : 0)));
					case ule -> currentBlock.addInst(new liInst(rd, new Imm(compareUnsigned(lhs_, rhs_) <= 0 ? 1 : 0)));
					case sgt -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ > rhs_ ? 1 : 0)));
					case sge -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ >= rhs_ ? 1 : 0)));
					case slt -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ < rhs_ ? 1 : 0)));
					case sle -> currentBlock.addInst(new liInst(rd, new Imm(lhs_ <= rhs_ ? 1 : 0)));
				}
			}

		} else if (stmt instanceof LLVMIR.Instruction.load) {
			load stmt_ = (load) stmt;
			if (stmt_.pointer instanceof globalVariable) {
				// TODO: 2021/3/27 deal with data segmentation 
			} else {
				currentBlock.addInst(new loadInst(
					((LLVMPointerType) stmt_.pointer.type).pointeeType.size() == 8 ? loadInst.loadType.LBU : loadInst.loadType.LW,
					registerMapping((register) stmt_.dest),
					registerMapping((register) stmt_.pointer),
					new Imm(0)
				));
			}
		} else if (stmt instanceof ret) {

		} else {
			assert stmt instanceof store;
			store stmt_ = (store) stmt;
			if (stmt_.dest instanceof globalVariable) {
				// TODO: 2021/3/27 deal with data segmentation
			} else {
				currentBlock.addInst(new storeInst(
					stmt_.value.type.size() == 8 ? storeInst.storeType.sb : storeInst.storeType.sw,
					registerMapping((register) stmt_.pointer),
					new Imm(0),
					registerMapping((register) stmt_.value)
				));
			}
		}
	}

	private void buildAsmBlock(basicBlock block) {
		asmBlock asmBlk = new asmBlock(++ blockCounter, block.name);
		currentFunction.asmBlocks.add(asmBlk);
		currentBlock = asmBlk;
		block.stmts.forEach(this::buildAsmInst);
		currentBlock = null;
	}

	private void buildAsmFunction(function func) {
		asmFunction asmFunc = new asmFunction(func.functionName);
		programAsmEntry.asmFunctions.add(asmFunc);
		currentFunction = asmFunc;
		func.blocks.forEach(this::buildAsmBlock);
		currentFunction = null;
	}

	@Override
	public void run() {
		// TODO: 2021/3/27 deal with global data
		programIREntry.functions.forEach(this::buildAsmFunction);
	}
}
