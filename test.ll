
@_g.n = global i32 0
@_g.p = global i32 0
@_g.k = global i32 0
@_g.i = global i32 0
@_g.w = global i32 3
@_g.v = global i32 0
@_strConst.0 = constant [ 4 x i8 ] c"<< \00"
@_strConst.1 = constant [ 2 x i8 ] c"(\00"
@_strConst.2 = constant [ 3 x i8 ] c") \00"
@_strConst.3 = constant [ 2 x i8 ] c" \00"
@_strConst.4 = constant [ 4 x i8 ] c">> \00"

define i32 @main() {
entry:
	%0 = load i32, i32* @_g.w
	%_int0 = add i32 0, i32 3
	%1 = add i32 %0, i32 %_int0
	store i32 %1, i32* @_g.v
	%_retVal0 = call i32 @_g.main()
	ret i32 %_retVal0
}

declare void @print(i8*)

declare void @println(i8*)

declare void @printInt(i32)

declare void @printlnInt(i32)

declare i8* @getString()

declare i32 @getInt()

declare i8* @toString(i32)

declare i8* @malloc(i64)

declare i8* @builtin.string.add(i8*, i8*)

declare i8 @builtin.string.isEqual(i8*, i8*)

declare i8 @builtin.string.isNotEqual(i8*, i8*)

declare i8 @builtin.string.isLessThan(i8*, i8*)

declare i8 @builtin.string.isGreaterThan(i8*, i8*)

declare i8 @builtin.string.isLessThanOrEqual(i8*, i8*)

declare i8 @builtin.string.isGreaterThanOrEqual(i8*, i8*)

declare i32 @builtin.string.length(i8*)

declare i8* @builtin.string.substring(i8*, i32, i32)

declare i32 @builtin.string.parseInt(i8*)

declare i32 @builtin.string.ord(i8*, i32)

define i32 @_g.main() {
entry:
	%0 = load i32, i32* @_g.n
	%_funcReturnValue0 = call i32 @getInt()
	store i32 %_funcReturnValue0, i32* @_g.n
	%0 = add i32 0, i32 %_funcReturnValue0
	%1 = load i32, i32* @_g.p
	%_funcReturnValue1 = call i32 @getInt()
	store i32 %_funcReturnValue1, i32* @_g.p
	%1 = add i32 0, i32 %_funcReturnValue1
	%2 = load i32, i32* @_g.k
	%_funcReturnValue2 = call i32 @getInt()
	store i32 %_funcReturnValue2, i32* @_g.k
	%2 = add i32 0, i32 %_funcReturnValue2
	%3 = load i32, i32* @_g.p
	%4 = load i32, i32* @_g.k
	%5 = sub i32 %3, i32 %4
	%_int0 = add i32 0, i32 1
	%6 = icmp sgt i32 %5, %_int0
	br i8 %6, label if.trueBranch0, label if.falseBranch0
if.trueBranch0:
	%_string0 = getelementptr [ 4 x i8 ], [ 4 x i8 ]* @_strConst.0, i32 0, i32 0
	call void @print(i8* %_string0)
	br label if.dest0
if.falseBranch0:
	br label if.dest0
if.dest0:
	%7 = load i32, i32* @_g.i
	%8 = load i32, i32* @_g.p
	%9 = load i32, i32* @_g.k
	%10 = sub i32 %8, i32 %9
	store i32 %10, i32* @_g.i
	%7 = add i32 0, i32 %10
	br label for.cond0
if.trueBranch2:
	%_string1 = getelementptr [ 2 x i8 ], [ 2 x i8 ]* @_strConst.1, i32 0, i32 0
	call void @print(i8* %_string1)
	%27 = load i32, i32* @_g.i
	%_funcReturnValue3 = call i8* @toString(i32 %27)
	call void @print(i8* %_funcReturnValue3)
	%_string2 = getelementptr [ 3 x i8 ], [ 3 x i8 ]* @_strConst.2, i32 0, i32 0
	call void @print(i8* %_string2)
	br label if.dest2
if.falseBranch2:
	%28 = load i32, i32* @_g.i
	call void @printInt(i32 %28)
	%_string3 = getelementptr [ 2 x i8 ], [ 2 x i8 ]* @_strConst.3, i32 0, i32 0
	call void @print(i8* %_string3)
	br label if.dest2
if.dest2:
	br label if.dest1
if.trueBranch1:
	%24 = load i32, i32* @_g.i
	%25 = load i32, i32* @_g.p
	%26 = icmp eq i32 %24, %25
	br i8 %26, label if.trueBranch2, label if.falseBranch2
if.falseBranch1:
	br label if.dest1
if.dest1:
	br label for.incr0
for.cond0:
	%11 = load i32, i32* @_g.i
	%12 = load i32, i32* @_g.p
	%13 = load i32, i32* @_g.k
	%14 = add i32 %12, i32 %13
	%15 = icmp sle i32 %11, %14
	br i8 %15, label for.body0, label for.dest0
for.body0:
	%_int1 = add i32 0, i32 1
	%18 = load i32, i32* @_g.i
	%19 = icmp sle i32 %_int1, %18
	%20 = load i32, i32* @_g.i
	%21 = load i32, i32* @_g.n
	%22 = icmp sle i32 %20, %21
	%23 = and i8 %19, i8 %22
	br i8 %23, label if.trueBranch1, label if.falseBranch1
for.incr0:
	%16 = load i32, i32* @_g.i
	%17 = add i32 0, i32 %16
	%16 = add i32 %16, i32 1
	store i32 %16, i32* @_g.i
for.dest0:
	%29 = load i32, i32* @_g.p
	%30 = load i32, i32* @_g.k
	%31 = add i32 %29, i32 %30
	%32 = load i32, i32* @_g.n
	%33 = icmp slt i32 %31, %32
	br i8 %33, label if.trueBranch3, label if.falseBranch3
if.trueBranch3:
	%_string4 = getelementptr [ 4 x i8 ], [ 4 x i8 ]* @_strConst.4, i32 0, i32 0
	call void @print(i8* %_string4)
	br label if.dest3
if.falseBranch3:
	br label if.dest3
if.dest3:
	%_int2 = add i32 0, i32 0
	ret i32 %_int2
}

