	.text
	.file	"builtin.c"
	.globl	print                   # -- Begin function print
	.p2align	2
	.type	print,@function
print:                                  # @print
	.cfi_startproc
# %bb.0:                                # %entry
	lui	a1, %hi(.L.str)
	addi	a1, a1, %lo(.L.str)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end0:
	.size	print, .Lfunc_end0-print
	.cfi_endproc
                                        # -- End function
	.globl	println                 # -- Begin function println
	.p2align	2
	.type	println,@function
println:                                # @println
	.cfi_startproc
# %bb.0:                                # %entry
	tail	puts
.Lfunc_end1:
	.size	println, .Lfunc_end1-println
	.cfi_endproc
                                        # -- End function
	.globl	printInt                # -- Begin function printInt
	.p2align	2
	.type	printInt,@function
printInt:                               # @printInt
	.cfi_startproc
# %bb.0:                                # %entry
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end2:
	.size	printInt, .Lfunc_end2-printInt
	.cfi_endproc
                                        # -- End function
	.globl	printlnInt              # -- Begin function printlnInt
	.p2align	2
	.type	printlnInt,@function
printlnInt:                             # @printlnInt
	.cfi_startproc
# %bb.0:                                # %entry
	lui	a1, %hi(.L.str.3)
	addi	a1, a1, %lo(.L.str.3)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end3:
	.size	printlnInt, .Lfunc_end3-printlnInt
	.cfi_endproc
                                        # -- End function
	.globl	getString               # -- Begin function getString
	.p2align	2
	.type	getString,@function
getString:                              # @getString
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	a0, zero, 1024
	mv	a1, zero
	call	malloc
	mv	s0, a0
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	mv	a1, s0
	call	__isoc99_scanf
	mv	a0, s0
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end4:
	.size	getString, .Lfunc_end4-getString
	.cfi_endproc
                                        # -- End function
	.globl	getInt                  # -- Begin function getInt
	.p2align	2
	.type	getInt,@function
getInt:                                 # @getInt
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	.cfi_offset ra, -4
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	addi	a1, sp, 8
	call	__isoc99_scanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end5:
	.size	getInt, .Lfunc_end5-getInt
	.cfi_endproc
                                        # -- End function
	.globl	toString                # -- Begin function toString
	.p2align	2
	.type	toString,@function
toString:                               # @toString
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	sw	s1, 4(sp)
	sw	s2, 0(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	.cfi_offset s2, -16
	mv	s0, a0
	addi	s2, zero, -1
	addi	a0, zero, 1024
	mv	a1, zero
	call	malloc
	beqz	s0, .LBB6_7
# %bb.1:                                # %for.body.preheader
	mv	s1, zero
	mv	a2, zero
	lui	a3, 419430
	addi	t0, a3, 1639
	addi	a6, zero, 10
	addi	a7, zero, 18
.LBB6_2:                                # %for.body
                                        # =>This Inner Loop Header: Depth=1
	mv	a1, s1
	mulh	s1, s0, t0
	srli	a4, s1, 31
	srai	s1, s1, 2
	add	a4, s1, a4
	mul	s1, a4, a6
	sub	s1, s0, s1
	addi	a5, s1, 48
	addi	s1, a1, 1
	sltu	a3, s1, a1
	add	a2, a2, a3
	add	a1, a0, a1
	addi	a3, s0, 9
	sb	a5, 0(a1)
	mv	s0, a4
	bltu	a7, a3, .LBB6_2
# %bb.3:                                # %for.end
	beqz	s1, .LBB6_7
# %bb.4:                                # %if.end
	addi	a1, zero, 2
	blt	s1, a1, .LBB6_8
# %bb.5:                                # %for.body7.preheader
	mv	a4, zero
	mv	a2, zero
	addi	a6, s1, -1
.LBB6_6:                                # %for.body7
                                        # =>This Inner Loop Header: Depth=1
	add	a1, a0, a4
	xor	a5, a4, s2
	add	a5, s1, a5
	add	a5, a0, a5
	lb	s0, 0(a5)
	lb	a3, 0(a1)
	sb	s0, 0(a1)
	sb	a3, 0(a5)
	addi	a1, a4, 1
	sltu	a3, a1, a4
	slli	a5, a1, 1
	add	a2, a2, a3
	mv	a4, a1
	blt	a5, a6, .LBB6_6
	j	.LBB6_8
.LBB6_7:                                # %if.end.thread
	addi	a1, zero, 48
	sb	a1, 0(a0)
	addi	s1, zero, 1
.LBB6_8:                                # %for.cond.cleanup
	and	a1, s1, s2
	add	a1, a0, a1
	sb	zero, 0(a1)
	lw	s2, 0(sp)
	lw	s1, 4(sp)
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end6:
	.size	toString, .Lfunc_end6-toString
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.add      # -- Begin function builtin.string.add
	.p2align	2
	.type	builtin.string.add,@function
builtin.string.add:                     # @builtin.string.add
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	sw	s1, 4(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	mv	s0, a1
	mv	s1, a0
	addi	a0, zero, 1024
	mv	a1, zero
	call	malloc
	lbu	a2, 0(s1)
	mv	a1, a0
	beqz	a2, .LBB7_3
# %bb.1:                                # %for.body.preheader
	addi	a3, s1, 1
	mv	a1, a0
.LBB7_2:                                # %for.body
                                        # =>This Inner Loop Header: Depth=1
	sb	a2, 0(a1)
	lbu	a2, 0(a3)
	addi	a1, a1, 1
	addi	a3, a3, 1
	bnez	a2, .LBB7_2
.LBB7_3:                                # %for.cond3.preheader
	lbu	a2, 0(s0)
	beqz	a2, .LBB7_6
# %bb.4:                                # %for.body7.preheader
	addi	a3, s0, 1
.LBB7_5:                                # %for.body7
                                        # =>This Inner Loop Header: Depth=1
	sb	a2, 0(a1)
	lbu	a2, 0(a3)
	addi	a1, a1, 1
	addi	a3, a3, 1
	bnez	a2, .LBB7_5
.LBB7_6:                                # %for.end11
	sb	zero, 0(a1)
	lw	s1, 4(sp)
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end7:
	.size	builtin.string.add, .Lfunc_end7-builtin.string.add
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isEqual  # -- Begin function builtin.string.isEqual
	.p2align	2
	.type	builtin.string.isEqual,@function
builtin.string.isEqual:                 # @builtin.string.isEqual
	.cfi_startproc
# %bb.0:                                # %entry
	mv	a2, a0
	lbu	a3, 0(a0)
	lbu	a4, 0(a1)
	or	a5, a3, a4
	addi	a0, zero, 1
	beqz	a5, .LBB8_4
# %bb.1:                                # %for.body.preheader
	addi	a1, a1, 1
	addi	a2, a2, 1
.LBB8_2:                                # %for.body
                                        # =>This Inner Loop Header: Depth=1
	andi	a4, a4, 255
	andi	a3, a3, 255
	bne	a3, a4, .LBB8_5
# %bb.3:                                # %for.inc
                                        #   in Loop: Header=BB8_2 Depth=1
	lbu	a3, 0(a2)
	lbu	a4, 0(a1)
	or	a5, a3, a4
	addi	a1, a1, 1
	addi	a2, a2, 1
	bnez	a5, .LBB8_2
.LBB8_4:                                # %return
	ret
.LBB8_5:
	mv	a0, zero
	ret
.Lfunc_end8:
	.size	builtin.string.isEqual, .Lfunc_end8-builtin.string.isEqual
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isNotEqual # -- Begin function builtin.string.isNotEqual
	.p2align	2
	.type	builtin.string.isNotEqual,@function
builtin.string.isNotEqual:              # @builtin.string.isNotEqual
	.cfi_startproc
# %bb.0:                                # %entry
	lbu	a2, 0(a0)
	lbu	a3, 0(a1)
	or	a4, a2, a3
	beqz	a4, .LBB9_4
# %bb.1:                                # %for.body.preheader
	addi	a1, a1, 1
	addi	a0, a0, 1
.LBB9_2:                                # %for.body
                                        # =>This Inner Loop Header: Depth=1
	andi	a3, a3, 255
	andi	a2, a2, 255
	bne	a2, a3, .LBB9_5
# %bb.3:                                # %for.inc
                                        #   in Loop: Header=BB9_2 Depth=1
	lbu	a2, 0(a0)
	lbu	a3, 0(a1)
	or	a4, a2, a3
	addi	a1, a1, 1
	addi	a0, a0, 1
	bnez	a4, .LBB9_2
.LBB9_4:
	mv	a0, zero
	ret
.LBB9_5:
	addi	a0, zero, 1
	ret
.Lfunc_end9:
	.size	builtin.string.isNotEqual, .Lfunc_end9-builtin.string.isNotEqual
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isLessThan # -- Begin function builtin.string.isLessThan
	.p2align	2
	.type	builtin.string.isLessThan,@function
builtin.string.isLessThan:              # @builtin.string.isLessThan
	.cfi_startproc
# %bb.0:                                # %entry
	lbu	a2, 0(a0)
	lbu	a3, 0(a1)
	or	a4, a2, a3
	beqz	a4, .LBB10_5
# %bb.1:                                # %for.body.preheader
	addi	a1, a1, 1
	addi	a0, a0, 1
.LBB10_2:                               # %for.body
                                        # =>This Inner Loop Header: Depth=1
	slli	a3, a3, 24
	srai	a3, a3, 24
	slli	a2, a2, 24
	srai	a2, a2, 24
	blt	a2, a3, .LBB10_6
# %bb.3:                                # %if.end
                                        #   in Loop: Header=BB10_2 Depth=1
	blt	a3, a2, .LBB10_5
# %bb.4:                                # %for.inc
                                        #   in Loop: Header=BB10_2 Depth=1
	lbu	a2, 0(a0)
	lbu	a3, 0(a1)
	or	a4, a2, a3
	addi	a1, a1, 1
	addi	a0, a0, 1
	bnez	a4, .LBB10_2
.LBB10_5:
	mv	a0, zero
	ret
.LBB10_6:
	addi	a0, zero, 1
	ret
.Lfunc_end10:
	.size	builtin.string.isLessThan, .Lfunc_end10-builtin.string.isLessThan
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isGreaterThan # -- Begin function builtin.string.isGreaterThan
	.p2align	2
	.type	builtin.string.isGreaterThan,@function
builtin.string.isGreaterThan:           # @builtin.string.isGreaterThan
	.cfi_startproc
# %bb.0:                                # %entry
	lbu	a3, 0(a0)
	lbu	a2, 0(a1)
	or	a4, a3, a2
	beqz	a4, .LBB11_5
# %bb.1:                                # %for.body.preheader
	addi	a1, a1, 1
	addi	a0, a0, 1
.LBB11_2:                               # %for.body
                                        # =>This Inner Loop Header: Depth=1
	slli	a3, a3, 24
	srai	a3, a3, 24
	slli	a2, a2, 24
	srai	a2, a2, 24
	blt	a2, a3, .LBB11_6
# %bb.3:                                # %if.end
                                        #   in Loop: Header=BB11_2 Depth=1
	blt	a3, a2, .LBB11_5
# %bb.4:                                # %for.inc
                                        #   in Loop: Header=BB11_2 Depth=1
	lbu	a3, 0(a0)
	lbu	a2, 0(a1)
	or	a4, a3, a2
	addi	a1, a1, 1
	addi	a0, a0, 1
	bnez	a4, .LBB11_2
.LBB11_5:
	mv	a0, zero
	ret
.LBB11_6:
	addi	a0, zero, 1
	ret
.Lfunc_end11:
	.size	builtin.string.isGreaterThan, .Lfunc_end11-builtin.string.isGreaterThan
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isLessThanOrEqual # -- Begin function builtin.string.isLessThanOrEqual
	.p2align	2
	.type	builtin.string.isLessThanOrEqual,@function
builtin.string.isLessThanOrEqual:       # @builtin.string.isLessThanOrEqual
	.cfi_startproc
# %bb.0:                                # %entry
	mv	a2, a0
	lb	a4, 0(a0)
	lb	a3, 0(a1)
	or	a0, a4, a3
	andi	a5, a0, 255
	addi	a0, zero, 1
	beqz	a5, .LBB12_6
# %bb.1:                                # %entry
	blt	a4, a3, .LBB12_6
# %bb.2:                                # %if.end.preheader
	addi	a1, a1, 1
	addi	a2, a2, 1
.LBB12_3:                               # %if.end
                                        # =>This Inner Loop Header: Depth=1
	slli	a4, a4, 24
	srai	a4, a4, 24
	slli	a3, a3, 24
	srai	a3, a3, 24
	blt	a3, a4, .LBB12_7
# %bb.4:                                # %for.inc
                                        #   in Loop: Header=BB12_3 Depth=1
	lb	a4, 0(a2)
	lb	a3, 0(a1)
	or	a5, a4, a3
	andi	a5, a5, 255
	beqz	a5, .LBB12_6
# %bb.5:                                # %for.inc
                                        #   in Loop: Header=BB12_3 Depth=1
	addi	a1, a1, 1
	addi	a2, a2, 1
	bge	a4, a3, .LBB12_3
.LBB12_6:                               # %return
	ret
.LBB12_7:
	mv	a0, zero
	ret
.Lfunc_end12:
	.size	builtin.string.isLessThanOrEqual, .Lfunc_end12-builtin.string.isLessThanOrEqual
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.isGreaterThanOrEqual # -- Begin function builtin.string.isGreaterThanOrEqual
	.p2align	2
	.type	builtin.string.isGreaterThanOrEqual,@function
builtin.string.isGreaterThanOrEqual:    # @builtin.string.isGreaterThanOrEqual
	.cfi_startproc
# %bb.0:                                # %entry
	mv	a2, a0
	lb	a3, 0(a0)
	lb	a4, 0(a1)
	or	a0, a3, a4
	andi	a5, a0, 255
	addi	a0, zero, 1
	beqz	a5, .LBB13_6
# %bb.1:                                # %entry
	blt	a4, a3, .LBB13_6
# %bb.2:                                # %if.end.preheader
	addi	a1, a1, 1
	addi	a2, a2, 1
.LBB13_3:                               # %if.end
                                        # =>This Inner Loop Header: Depth=1
	slli	a4, a4, 24
	srai	a4, a4, 24
	slli	a3, a3, 24
	srai	a3, a3, 24
	blt	a3, a4, .LBB13_7
# %bb.4:                                # %for.inc
                                        #   in Loop: Header=BB13_3 Depth=1
	lb	a3, 0(a2)
	lb	a4, 0(a1)
	or	a5, a3, a4
	andi	a5, a5, 255
	beqz	a5, .LBB13_6
# %bb.5:                                # %for.inc
                                        #   in Loop: Header=BB13_3 Depth=1
	addi	a1, a1, 1
	addi	a2, a2, 1
	bge	a4, a3, .LBB13_3
.LBB13_6:                               # %return
	ret
.LBB13_7:
	mv	a0, zero
	ret
.Lfunc_end13:
	.size	builtin.string.isGreaterThanOrEqual, .Lfunc_end13-builtin.string.isGreaterThanOrEqual
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.length   # -- Begin function builtin.string.length
	.p2align	2
	.type	builtin.string.length,@function
builtin.string.length:                  # @builtin.string.length
	.cfi_startproc
# %bb.0:                                # %entry
	mv	a1, a0
	lbu	a0, 0(a0)
	beqz	a0, .LBB14_4
# %bb.1:                                # %for.inc.preheader
	mv	a0, zero
	addi	a1, a1, 1
.LBB14_2:                               # %for.inc
                                        # =>This Inner Loop Header: Depth=1
	add	a2, a1, a0
	lbu	a2, 0(a2)
	addi	a0, a0, 1
	bnez	a2, .LBB14_2
# %bb.3:                                # %for.end
	ret
.LBB14_4:
	mv	a0, zero
	ret
.Lfunc_end14:
	.size	builtin.string.length, .Lfunc_end14-builtin.string.length
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.substring # -- Begin function builtin.string.substring
	.p2align	2
	.type	builtin.string.substring,@function
builtin.string.substring:               # @builtin.string.substring
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -128
	.cfi_def_cfa_offset 128
	sw	ra, 124(sp)
	sw	s0, 120(sp)
	sw	s1, 116(sp)
	sw	s2, 112(sp)
	sw	s3, 108(sp)
	sw	s4, 104(sp)
	sw	s5, 100(sp)
	sw	s6, 96(sp)
	sw	s7, 92(sp)
	sw	s8, 88(sp)
	sw	s9, 84(sp)
	sw	s10, 80(sp)
	sw	s11, 76(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	.cfi_offset s1, -12
	.cfi_offset s2, -16
	.cfi_offset s3, -20
	.cfi_offset s4, -24
	.cfi_offset s5, -28
	.cfi_offset s6, -32
	.cfi_offset s7, -36
	.cfi_offset s8, -40
	.cfi_offset s9, -44
	.cfi_offset s10, -48
	.cfi_offset s11, -52
	mv	s2, a2
	mv	s1, a1
	mv	s0, a0
	addi	a0, zero, 1024
	mv	a1, zero
	call	malloc
	sub	a1, s2, s1
	beqz	s1, .LBB15_2
# %bb.1:                                # %entry
	add	s0, s1, s0
.LBB15_2:                               # %entry
	beqz	a1, .LBB15_12
# %bb.3:                                # %for.body4.preheader
	not	a2, s1
	add	a2, a2, s2
	addi	a3, zero, 31
	bltu	a2, a3, .LBB15_6
# %bb.4:                                # %vector.memcheck
	sub	a4, s2, s1
	add	a3, s0, a4
	bgeu	a0, a3, .LBB15_14
# %bb.5:                                # %vector.memcheck
	add	a3, a0, a4
	bgeu	s0, a3, .LBB15_14
.LBB15_6:
	mv	s1, zero
	mv	a2, zero
.LBB15_7:                               # %for.body4.preheader44
	andi	a5, a1, 3
	addi	a6, a1, -1
	beqz	a5, .LBB15_10
# %bb.8:                                # %for.body4.prol.preheader
	mv	a3, s1
.LBB15_9:                               # %for.body4.prol
                                        # =>This Inner Loop Header: Depth=1
	lb	a4, 0(s0)
	add	s1, a0, a3
	sb	a4, 0(s1)
	addi	s0, s0, 1
	addi	a1, a1, -1
	addi	s1, a3, 1
	sltu	a3, s1, a3
	addi	a5, a5, -1
	add	a2, a2, a3
	mv	a3, s1
	bnez	a5, .LBB15_9
.LBB15_10:                              # %for.body4.prol.loopexit
	addi	a4, zero, 3
	mv	a3, s1
	bltu	a6, a4, .LBB15_13
.LBB15_11:                              # %for.body4
                                        # =>This Inner Loop Header: Depth=1
	lb	a4, 0(s0)
	add	a3, a0, s1
	sb	a4, 0(a3)
	lb	a4, 1(s0)
	sb	a4, 1(a3)
	lb	a4, 2(s0)
	sb	a4, 2(a3)
	lb	a4, 3(s0)
	sb	a4, 3(a3)
	addi	a1, a1, -4
	addi	a3, s1, 4
	sltu	a4, a3, s1
	add	a2, a2, a4
	addi	s0, s0, 4
	mv	s1, a3
	bnez	a1, .LBB15_11
	j	.LBB15_13
.LBB15_12:
	mv	a3, zero
.LBB15_13:                              # %for.end8
	add	a1, a0, a3
	sb	zero, 0(a1)
	lw	s11, 76(sp)
	lw	s10, 80(sp)
	lw	s9, 84(sp)
	lw	s8, 88(sp)
	lw	s7, 92(sp)
	lw	s6, 96(sp)
	lw	s5, 100(sp)
	lw	s4, 104(sp)
	lw	s3, 108(sp)
	lw	s2, 112(sp)
	lw	s1, 116(sp)
	lw	s0, 120(sp)
	lw	ra, 124(sp)
	addi	sp, sp, 128
	ret
.LBB15_14:                              # %vector.ph
	sw	a0, 72(sp)
	sltu	a2, a4, a2
	sw	a4, 4(sp)
	andi	s1, a4, -32
	addi	a3, s1, -32
	sltu	a4, a3, s1
	add	a4, a2, a4
	addi	a7, a4, -1
	slli	a4, a7, 27
	srli	a5, a3, 5
	or	a5, a5, a4
	addi	t0, a5, 1
	beqz	a7, .LBB15_16
# %bb.15:                               # %vector.ph
	mv	a3, zero
	j	.LBB15_17
.LBB15_16:
	sltiu	a3, a3, 96
.LBB15_17:                              # %vector.ph
	andi	a4, t0, 3
	sw	a4, 0(sp)
	beqz	a3, .LBB15_19
# %bb.18:
	mv	a5, zero
	mv	a6, zero
	j	.LBB15_21
.LBB15_19:                              # %vector.ph.new
	mv	a0, zero
	mv	a6, zero
	srli	a3, a7, 5
	sltu	a5, t0, a5
	add	a7, a3, a5
	sub	a3, t0, a4
	sltu	a5, t0, a4
	sub	a4, a7, a5
.LBB15_20:                              # %vector.body
                                        # =>This Inner Loop Header: Depth=1
	sw	a3, 56(sp)
	sw	a4, 60(sp)
	sw	a6, 64(sp)
	add	a5, s0, a0
	lb	a4, 0(a5)
	lb	a3, 1(a5)
	sw	a3, 48(sp)
	lb	a3, 2(a5)
	sw	a3, 40(sp)
	lb	a3, 3(a5)
	sw	a3, 32(sp)
	lb	a3, 4(a5)
	sw	a3, 24(sp)
	lb	a3, 5(a5)
	sw	a3, 16(sp)
	lb	s5, 6(a5)
	lb	s6, 7(a5)
	lb	s7, 8(a5)
	lb	s8, 9(a5)
	lb	s9, 10(a5)
	lb	s10, 11(a5)
	lb	s11, 12(a5)
	lb	ra, 13(a5)
	lb	s4, 14(a5)
	lb	a6, 15(a5)
	lb	a3, 16(a5)
	sw	a3, 52(sp)
	lb	a3, 17(a5)
	sw	a3, 44(sp)
	lb	a3, 18(a5)
	sw	a3, 36(sp)
	lb	a3, 19(a5)
	sw	a3, 28(sp)
	lb	a3, 20(a5)
	sw	a3, 20(sp)
	lb	a3, 21(a5)
	sw	a3, 12(sp)
	lb	s2, 22(a5)
	lb	s3, 23(a5)
	lb	t2, 24(a5)
	lb	t3, 25(a5)
	lb	t0, 26(a5)
	lb	t4, 27(a5)
	lb	t1, 28(a5)
	lb	t5, 29(a5)
	lb	a7, 30(a5)
	lb	a5, 31(a5)
	lw	a3, 72(sp)
	add	t6, a3, a0
	sw	a0, 68(sp)
	sb	a6, 15(t6)
	sb	s4, 14(t6)
	sb	ra, 13(t6)
	sb	s11, 12(t6)
	sb	s10, 11(t6)
	sb	s9, 10(t6)
	sb	s8, 9(t6)
	sb	s7, 8(t6)
	sb	s6, 7(t6)
	sb	s5, 6(t6)
	lw	a3, 16(sp)
	sb	a3, 5(t6)
	lw	a3, 24(sp)
	sb	a3, 4(t6)
	lw	a3, 32(sp)
	sb	a3, 3(t6)
	lw	a3, 40(sp)
	sb	a3, 2(t6)
	lw	a3, 48(sp)
	sb	a3, 1(t6)
	sb	a4, 0(t6)
	sb	a5, 31(t6)
	sb	a7, 30(t6)
	sb	t5, 29(t6)
	sb	t1, 28(t6)
	sb	t4, 27(t6)
	sb	t0, 26(t6)
	sb	t3, 25(t6)
	sb	t2, 24(t6)
	sb	s3, 23(t6)
	sb	s2, 22(t6)
	lw	a3, 12(sp)
	sb	a3, 21(t6)
	lw	a3, 20(sp)
	sb	a3, 20(t6)
	lw	a3, 28(sp)
	sb	a3, 19(t6)
	lw	a3, 36(sp)
	sb	a3, 18(t6)
	lw	a3, 44(sp)
	sb	a3, 17(t6)
	lw	a3, 52(sp)
	sb	a3, 16(t6)
	ori	a0, a0, 32
	add	a3, s0, a0
	lb	a4, 0(a3)
	sw	a4, 52(sp)
	lb	a4, 1(a3)
	sw	a4, 44(sp)
	lb	a4, 2(a3)
	sw	a4, 36(sp)
	lb	a4, 3(a3)
	sw	a4, 28(sp)
	lb	a4, 4(a3)
	sw	a4, 20(sp)
	lb	ra, 5(a3)
	lb	s10, 6(a3)
	lb	s9, 7(a3)
	lb	s2, 8(a3)
	lb	s3, 9(a3)
	lb	s5, 10(a3)
	lb	s6, 11(a3)
	lb	s7, 12(a3)
	lb	s8, 13(a3)
	lb	a7, 14(a3)
	lb	a5, 15(a3)
	lb	a4, 16(a3)
	sw	a4, 48(sp)
	lb	a4, 17(a3)
	sw	a4, 40(sp)
	lb	a4, 18(a3)
	sw	a4, 32(sp)
	lb	a4, 19(a3)
	sw	a4, 24(sp)
	lb	a4, 20(a3)
	sw	a4, 16(sp)
	lb	a4, 21(a3)
	sw	a4, 12(sp)
	lb	s11, 22(a3)
	lb	s4, 23(a3)
	lb	t6, 24(a3)
	lb	t5, 25(a3)
	lb	t4, 26(a3)
	lb	t3, 27(a3)
	lb	t2, 28(a3)
	lb	t0, 29(a3)
	lb	a6, 30(a3)
	lb	a3, 31(a3)
	lw	t1, 72(sp)
	add	a0, t1, a0
	sb	a5, 15(a0)
	sb	a7, 14(a0)
	sb	s8, 13(a0)
	sb	s7, 12(a0)
	sb	s6, 11(a0)
	sb	s5, 10(a0)
	sb	s3, 9(a0)
	sb	s2, 8(a0)
	sb	s9, 7(a0)
	sb	s10, 6(a0)
	sb	ra, 5(a0)
	lw	a4, 20(sp)
	sb	a4, 4(a0)
	lw	a4, 28(sp)
	sb	a4, 3(a0)
	lw	a4, 36(sp)
	sb	a4, 2(a0)
	lw	a4, 44(sp)
	sb	a4, 1(a0)
	lw	a4, 52(sp)
	sb	a4, 0(a0)
	sb	a3, 31(a0)
	sb	a6, 30(a0)
	sb	t0, 29(a0)
	sb	t2, 28(a0)
	sb	t3, 27(a0)
	sb	t4, 26(a0)
	sb	t5, 25(a0)
	sb	t6, 24(a0)
	sb	s4, 23(a0)
	sb	s11, 22(a0)
	lw	a3, 12(sp)
	sb	a3, 21(a0)
	lw	a3, 16(sp)
	sb	a3, 20(a0)
	lw	a3, 24(sp)
	sb	a3, 19(a0)
	lw	a3, 32(sp)
	sb	a3, 18(a0)
	lw	a3, 40(sp)
	sb	a3, 17(a0)
	lw	a3, 48(sp)
	sb	a3, 16(a0)
	lw	t0, 68(sp)
	ori	a0, t0, 64
	add	a3, s0, a0
	lb	a4, 0(a3)
	sw	a4, 52(sp)
	lb	a4, 1(a3)
	sw	a4, 44(sp)
	lb	a4, 2(a3)
	sw	a4, 36(sp)
	lb	a4, 3(a3)
	sw	a4, 28(sp)
	lb	a4, 4(a3)
	sw	a4, 20(sp)
	lb	a4, 5(a3)
	sw	a4, 12(sp)
	lb	ra, 6(a3)
	lb	s10, 7(a3)
	lb	s8, 8(a3)
	lb	s2, 9(a3)
	lb	s3, 10(a3)
	lb	s4, 11(a3)
	lb	s5, 12(a3)
	lb	s6, 13(a3)
	lb	a7, 14(a3)
	lb	a5, 15(a3)
	lb	a4, 16(a3)
	sw	a4, 48(sp)
	lb	a4, 17(a3)
	sw	a4, 40(sp)
	lb	a4, 18(a3)
	sw	a4, 32(sp)
	lb	a4, 19(a3)
	sw	a4, 24(sp)
	lb	a4, 20(a3)
	sw	a4, 16(sp)
	lb	a4, 21(a3)
	sw	a4, 8(sp)
	lb	s11, 22(a3)
	lb	s9, 23(a3)
	lb	s7, 24(a3)
	lb	t6, 25(a3)
	lb	t5, 26(a3)
	lb	t4, 27(a3)
	lb	t3, 28(a3)
	lb	t2, 29(a3)
	lb	a6, 30(a3)
	lb	a3, 31(a3)
	add	a0, t1, a0
	sb	a5, 15(a0)
	sb	a7, 14(a0)
	sb	s6, 13(a0)
	sb	s5, 12(a0)
	sb	s4, 11(a0)
	sb	s3, 10(a0)
	sb	s2, 9(a0)
	sb	s8, 8(a0)
	sb	s10, 7(a0)
	sb	ra, 6(a0)
	lw	a4, 12(sp)
	sb	a4, 5(a0)
	lw	a4, 20(sp)
	sb	a4, 4(a0)
	lw	a4, 28(sp)
	sb	a4, 3(a0)
	lw	a4, 36(sp)
	sb	a4, 2(a0)
	lw	a4, 44(sp)
	sb	a4, 1(a0)
	lw	a4, 52(sp)
	sb	a4, 0(a0)
	sb	a3, 31(a0)
	sb	a6, 30(a0)
	sb	t2, 29(a0)
	sb	t3, 28(a0)
	sb	t4, 27(a0)
	sb	t5, 26(a0)
	sb	t6, 25(a0)
	sb	s7, 24(a0)
	sb	s9, 23(a0)
	sb	s11, 22(a0)
	lw	a3, 8(sp)
	sb	a3, 21(a0)
	lw	a3, 16(sp)
	sb	a3, 20(a0)
	lw	a3, 24(sp)
	sb	a3, 19(a0)
	lw	a3, 32(sp)
	sb	a3, 18(a0)
	lw	a3, 40(sp)
	sb	a3, 17(a0)
	lw	a3, 48(sp)
	sb	a3, 16(a0)
	ori	a0, t0, 96
	add	a3, s0, a0
	lb	a4, 0(a3)
	sw	a4, 52(sp)
	lb	a4, 1(a3)
	sw	a4, 44(sp)
	lb	a4, 2(a3)
	sw	a4, 36(sp)
	lb	a4, 3(a3)
	sw	a4, 28(sp)
	lb	a4, 4(a3)
	sw	a4, 20(sp)
	lb	a4, 5(a3)
	sw	a4, 12(sp)
	lb	s10, 6(a3)
	lb	s8, 7(a3)
	lb	t6, 8(a3)
	lb	s2, 9(a3)
	lb	s3, 10(a3)
	lb	s4, 11(a3)
	lb	s5, 12(a3)
	lb	s6, 13(a3)
	lb	a7, 14(a3)
	lb	a5, 15(a3)
	lb	a4, 16(a3)
	sw	a4, 48(sp)
	lb	a4, 17(a3)
	sw	a4, 40(sp)
	lb	a4, 18(a3)
	sw	a4, 32(sp)
	lb	a4, 19(a3)
	sw	a4, 24(sp)
	lb	a4, 20(a3)
	sw	a4, 16(sp)
	lb	ra, 21(a3)
	lb	s9, 22(a3)
	lb	s7, 23(a3)
	lb	t5, 24(a3)
	lb	t4, 25(a3)
	lb	t3, 26(a3)
	lb	t2, 27(a3)
	lb	t1, 28(a3)
	lb	t0, 29(a3)
	lb	a6, 30(a3)
	lb	a3, 31(a3)
	lw	s11, 72(sp)
	add	a0, s11, a0
	sb	a5, 15(a0)
	sb	a7, 14(a0)
	sb	s6, 13(a0)
	sb	s5, 12(a0)
	sb	s4, 11(a0)
	sb	s3, 10(a0)
	sb	s2, 9(a0)
	sb	t6, 8(a0)
	sb	s8, 7(a0)
	sb	s10, 6(a0)
	lw	a4, 12(sp)
	sb	a4, 5(a0)
	lw	a4, 20(sp)
	sb	a4, 4(a0)
	lw	a4, 28(sp)
	sb	a4, 3(a0)
	lw	a4, 36(sp)
	sb	a4, 2(a0)
	lw	a4, 44(sp)
	sb	a4, 1(a0)
	lw	a4, 52(sp)
	sb	a4, 0(a0)
	sb	a3, 31(a0)
	sb	a6, 30(a0)
	lw	a6, 64(sp)
	sb	t0, 29(a0)
	sb	t1, 28(a0)
	sb	t2, 27(a0)
	sb	t3, 26(a0)
	sb	t4, 25(a0)
	sb	t5, 24(a0)
	sb	s7, 23(a0)
	sb	s9, 22(a0)
	sb	ra, 21(a0)
	lw	a3, 16(sp)
	sb	a3, 20(a0)
	lw	a3, 24(sp)
	sb	a3, 19(a0)
	lw	a3, 32(sp)
	sb	a3, 18(a0)
	lw	a3, 40(sp)
	sb	a3, 17(a0)
	lw	a3, 48(sp)
	sb	a3, 16(a0)
	lw	a0, 68(sp)
	addi	a5, a0, 128
	sltu	t0, a5, a0
	lw	a4, 56(sp)
	addi	a3, a4, -4
	sltu	a4, a3, a4
	lw	a0, 60(sp)
	add	a4, a0, a4
	addi	a4, a4, -1
	or	a7, a3, a4
	add	a6, a6, t0
	mv	a0, a5
	bnez	a7, .LBB15_20
.LBB15_21:                              # %middle.block.unr-lcssa
	lw	s11, 0(sp)
	beqz	s11, .LBB15_24
# %bb.22:                               # %vector.body.epil.preheader
	mv	ra, zero
.LBB15_23:                              # %vector.body.epil
                                        # =>This Inner Loop Header: Depth=1
	add	a0, s0, a5
	lb	a3, 0(a0)
	sw	a3, 68(sp)
	lb	a3, 1(a0)
	sw	a3, 60(sp)
	lb	a3, 2(a0)
	sw	a3, 52(sp)
	lb	a3, 3(a0)
	sw	a3, 44(sp)
	lb	a3, 4(a0)
	sw	a3, 36(sp)
	lb	a3, 5(a0)
	sw	a3, 28(sp)
	lb	a3, 6(a0)
	sw	a3, 20(sp)
	lb	s9, 7(a0)
	lb	t6, 8(a0)
	lb	s2, 9(a0)
	lb	s3, 10(a0)
	lb	s4, 11(a0)
	lb	s5, 12(a0)
	lb	s6, 13(a0)
	lb	s7, 14(a0)
	lb	t0, 15(a0)
	lb	a3, 16(a0)
	sw	a3, 64(sp)
	lb	a3, 17(a0)
	sw	a3, 56(sp)
	lb	a3, 18(a0)
	sw	a3, 48(sp)
	lb	a3, 19(a0)
	sw	a3, 40(sp)
	lb	a3, 20(a0)
	sw	a3, 32(sp)
	lb	a3, 21(a0)
	sw	a3, 24(sp)
	lb	a3, 22(a0)
	sw	a3, 16(sp)
	lb	s10, 23(a0)
	lb	s8, 24(a0)
	lb	t5, 25(a0)
	lb	t4, 26(a0)
	lb	t3, 27(a0)
	lb	t2, 28(a0)
	lb	t1, 29(a0)
	mv	a7, a6
	lb	a6, 30(a0)
	lb	a0, 31(a0)
	lw	a4, 72(sp)
	add	a4, a4, a5
	sb	t0, 15(a4)
	sb	s7, 14(a4)
	sb	s6, 13(a4)
	sb	s5, 12(a4)
	sb	s4, 11(a4)
	sb	s3, 10(a4)
	sb	s2, 9(a4)
	sb	t6, 8(a4)
	sb	s9, 7(a4)
	lw	a3, 20(sp)
	sb	a3, 6(a4)
	lw	a3, 28(sp)
	sb	a3, 5(a4)
	lw	a3, 36(sp)
	sb	a3, 4(a4)
	lw	a3, 44(sp)
	sb	a3, 3(a4)
	lw	a3, 52(sp)
	sb	a3, 2(a4)
	lw	a3, 60(sp)
	sb	a3, 1(a4)
	lw	a3, 68(sp)
	sb	a3, 0(a4)
	sb	a0, 31(a4)
	sb	a6, 30(a4)
	sb	t1, 29(a4)
	sb	t2, 28(a4)
	sb	t3, 27(a4)
	sb	t4, 26(a4)
	sb	t5, 25(a4)
	sb	s8, 24(a4)
	sb	s10, 23(a4)
	lw	a0, 16(sp)
	sb	a0, 22(a4)
	lw	a0, 24(sp)
	sb	a0, 21(a4)
	lw	a0, 32(sp)
	sb	a0, 20(a4)
	lw	a0, 40(sp)
	sb	a0, 19(a4)
	lw	a0, 48(sp)
	sb	a0, 18(a4)
	lw	a0, 56(sp)
	sb	a0, 17(a4)
	lw	a0, 64(sp)
	sb	a0, 16(a4)
	addi	a0, a5, 32
	sltu	a6, a0, a5
	addi	a4, s11, -1
	sltu	a5, a4, s11
	add	a5, ra, a5
	addi	ra, a5, -1
	or	a3, a4, ra
	add	a6, a7, a6
	mv	a5, a0
	mv	s11, a4
	bnez	a3, .LBB15_23
.LBB15_24:                              # %middle.block
	lw	a0, 4(sp)
	xor	a0, a0, s1
	xor	a3, a2, a2
	or	a0, a0, a3
	bnez	a0, .LBB15_26
# %bb.25:
	mv	a3, s1
	lw	a0, 72(sp)
	j	.LBB15_13
.LBB15_26:
	add	s0, s0, s1
	sub	a1, a1, s1
	lw	a0, 72(sp)
	j	.LBB15_7
.Lfunc_end15:
	.size	builtin.string.substring, .Lfunc_end15-builtin.string.substring
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.parseInt # -- Begin function builtin.string.parseInt
	.p2align	2
	.type	builtin.string.parseInt,@function
builtin.string.parseInt:                # @builtin.string.parseInt
	.cfi_startproc
# %bb.0:                                # %entry
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	mv	s0, a0
	call	__ctype_b_loc
	lbu	a2, 0(s0)
	lw	a1, 0(a0)
	slli	a0, a2, 24
	srai	a0, a0, 24
	slli	a0, a0, 1
	add	a0, a1, a0
	lbu	a0, 1(a0)
	andi	a0, a0, 8
	beqz	a0, .LBB16_3
# %bb.1:                                # %for.body.preheader
	mv	a0, zero
	lui	a3, 1
	addi	a3, a3, -2048
	addi	a4, s0, 1
	addi	a6, zero, 10
.LBB16_2:                               # %for.body
                                        # =>This Inner Loop Header: Depth=1
	lb	s0, 0(a4)
	slli	a2, a2, 24
	srai	a2, a2, 24
	mul	a0, a0, a6
	slli	a5, s0, 1
	add	a5, a1, a5
	lhu	a5, 0(a5)
	add	a0, a0, a2
	addi	a0, a0, -48
	andi	a2, s0, 255
	and	a5, a5, a3
	addi	a4, a4, 1
	bnez	a5, .LBB16_2
	j	.LBB16_4
.LBB16_3:
	mv	a0, zero
.LBB16_4:                               # %for.end
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end16:
	.size	builtin.string.parseInt, .Lfunc_end16-builtin.string.parseInt
	.cfi_endproc
                                        # -- End function
	.globl	builtin.string.ord      # -- Begin function builtin.string.ord
	.p2align	2
	.type	builtin.string.ord,@function
builtin.string.ord:                     # @builtin.string.ord
	.cfi_startproc
# %bb.0:                                # %entry
	beqz	a1, .LBB17_2
# %bb.1:                                # %entry
	add	a0, a1, a0
.LBB17_2:                               # %entry
	lb	a0, 0(a0)
	ret
.Lfunc_end17:
	.size	builtin.string.ord, .Lfunc_end17-builtin.string.ord
	.cfi_endproc
                                        # -- End function
	.type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%s"
	.size	.L.str, 3

	.type	.L.str.2,@object        # @.str.2
.L.str.2:
	.asciz	"%d"
	.size	.L.str.2, 3

	.type	.L.str.3,@object        # @.str.3
.L.str.3:
	.asciz	"%d\n"
	.size	.L.str.3, 4

	.ident	"clang version 10.0.0-4ubuntu1~18.04.2 "
	.section	".note.GNU-stack","",@progbits
