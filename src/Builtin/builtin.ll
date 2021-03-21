; ModuleID = 'builtin.c'
source_filename = "builtin.c"
target datalayout = "e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

@.str = private unnamed_addr constant [3 x i8] c"%s\00", align 1
@.str.2 = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.3 = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1

; Function Attrs: nofree nounwind uwtable
define dso_local void @print(i8* %s) local_unnamed_addr #0 {
entry:
  %call = tail call i32 (i8*, ...) @printf(i8* nonnull dereferenceable(1) getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i64 0, i64 0), i8* %s)
  ret void
}

; Function Attrs: nofree nounwind
declare dso_local i32 @printf(i8* nocapture readonly, ...) local_unnamed_addr #1

; Function Attrs: nofree nounwind uwtable
define dso_local void @println(i8* nocapture readonly %s) local_unnamed_addr #0 {
entry:
  %puts = tail call i32 @puts(i8* nonnull dereferenceable(1) %s)
  ret void
}

; Function Attrs: nofree nounwind uwtable
define dso_local void @printInt(i32 %n) local_unnamed_addr #0 {
entry:
  %call = tail call i32 (i8*, ...) @printf(i8* nonnull dereferenceable(1) getelementptr inbounds ([3 x i8], [3 x i8]* @.str.2, i64 0, i64 0), i32 %n)
  ret void
}

; Function Attrs: nofree nounwind uwtable
define dso_local void @printlnInt(i32 %n) local_unnamed_addr #0 {
entry:
  %call = tail call i32 (i8*, ...) @printf(i8* nonnull dereferenceable(1) getelementptr inbounds ([4 x i8], [4 x i8]* @.str.3, i64 0, i64 0), i32 %n)
  ret void
}

; Function Attrs: nofree nounwind uwtable
define dso_local i8* @getString() local_unnamed_addr #0 {
entry:
  %call = tail call noalias dereferenceable_or_null(1024) i8* @malloc(i64 1024) #8
  %call1 = tail call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i64 0, i64 0), i8* %call)
  ret i8* %call
}

; Function Attrs: argmemonly nounwind willreturn
declare void @llvm.lifetime.start.p0i8(i64 immarg, i8* nocapture) #2

; Function Attrs: nofree nounwind
declare dso_local noalias i8* @malloc(i64) local_unnamed_addr #1

; Function Attrs: nofree nounwind
declare dso_local i32 @__isoc99_scanf(i8* nocapture readonly, ...) local_unnamed_addr #1

; Function Attrs: argmemonly nounwind willreturn
declare void @llvm.lifetime.end.p0i8(i64 immarg, i8* nocapture) #2

; Function Attrs: nounwind uwtable
define dso_local i32 @getInt() local_unnamed_addr #3 {
entry:
  %ret = alloca i32, align 4
  %0 = bitcast i32* %ret to i8*
  call void @llvm.lifetime.start.p0i8(i64 4, i8* nonnull %0) #8
  %call = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.2, i64 0, i64 0), i32* nonnull %ret)
  %1 = load i32, i32* %ret, align 4, !tbaa !2
  call void @llvm.lifetime.end.p0i8(i64 4, i8* nonnull %0) #8
  ret i32 %1
}

; Function Attrs: nofree nounwind uwtable
define dso_local noalias i8* @toString(i32 %i) local_unnamed_addr #0 {
entry:
  %call = tail call noalias dereferenceable_or_null(1024) i8* @malloc(i64 1024) #8
  %tobool50 = icmp eq i32 %i, 0
  br i1 %tobool50, label %if.end.thread, label %for.body

for.body:                                         ; preds = %entry, %for.body
  %indvars.iv54 = phi i64 [ %indvars.iv.next55, %for.body ], [ 0, %entry ]
  %i.addr.052 = phi i32 [ %div, %for.body ], [ %i, %entry ]
  %rem = srem i32 %i.addr.052, 10
  %0 = trunc i32 %rem to i8
  %conv = add nsw i8 %0, 48
  %indvars.iv.next55 = add nuw i64 %indvars.iv54, 1
  %arrayidx = getelementptr inbounds i8, i8* %call, i64 %indvars.iv54
  store i8 %conv, i8* %arrayidx, align 1, !tbaa !6
  %div = sdiv i32 %i.addr.052, 10
  %i.addr.052.off = add i32 %i.addr.052, 9
  %1 = icmp ult i32 %i.addr.052.off, 19
  br i1 %1, label %for.end, label %for.body

for.end:                                          ; preds = %for.body
  %2 = trunc i64 %indvars.iv.next55 to i32
  %tobool1 = icmp eq i32 %2, 0
  br i1 %tobool1, label %if.end.thread, label %if.end

if.end.thread:                                    ; preds = %for.end, %entry
  store i8 48, i8* %call, align 1, !tbaa !6
  br label %for.cond.cleanup

if.end:                                           ; preds = %for.end
  %sub = add nsw i32 %2, -1
  %cmp48 = icmp sgt i32 %2, 1
  br i1 %cmp48, label %for.body7, label %for.cond.cleanup

for.cond.cleanup:                                 ; preds = %for.body7, %if.end.thread, %if.end
  %len.161 = phi i64 [ 1, %if.end.thread ], [ %indvars.iv.next55, %if.end ], [ %indvars.iv.next55, %for.body7 ]
  %idxprom23 = and i64 %len.161, 4294967295
  %arrayidx24 = getelementptr inbounds i8, i8* %call, i64 %idxprom23
  store i8 0, i8* %arrayidx24, align 1, !tbaa !6
  ret i8* %call

for.body7:                                        ; preds = %if.end, %for.body7
  %indvars.iv = phi i64 [ %indvars.iv.next, %for.body7 ], [ 0, %if.end ]
  %arrayidx9 = getelementptr inbounds i8, i8* %call, i64 %indvars.iv
  %3 = load i8, i8* %arrayidx9, align 1, !tbaa !6
  %4 = xor i64 %indvars.iv, 4294967295
  %sub11 = add i64 %indvars.iv.next55, %4
  %sext = shl i64 %sub11, 32
  %idxprom12 = ashr exact i64 %sext, 32
  %arrayidx13 = getelementptr inbounds i8, i8* %call, i64 %idxprom12
  %5 = load i8, i8* %arrayidx13, align 1, !tbaa !6
  store i8 %5, i8* %arrayidx9, align 1, !tbaa !6
  store i8 %3, i8* %arrayidx13, align 1, !tbaa !6
  %indvars.iv.next = add nuw i64 %indvars.iv, 1
  %indvars.iv.next.tr = trunc i64 %indvars.iv.next to i32
  %6 = shl i32 %indvars.iv.next.tr, 1
  %cmp = icmp slt i32 %6, %sub
  br i1 %cmp, label %for.body7, label %for.cond.cleanup
}

; Function Attrs: nofree nounwind uwtable
define dso_local noalias i8* @builtin.string.add(i8* nocapture readonly %lhs, i8* nocapture readonly %rhs) local_unnamed_addr #0 {
entry:
  %call = tail call noalias dereferenceable_or_null(1024) i8* @malloc(i64 1024) #8
  %0 = load i8, i8* %lhs, align 1, !tbaa !6
  %cmp26 = icmp eq i8 %0, 0
  br i1 %cmp26, label %for.cond3.preheader, label %for.body

for.cond3.preheader:                              ; preds = %for.body, %entry
  %ptr.0.lcssa = phi i8* [ %call, %entry ], [ %incdec.ptr2, %for.body ]
  %1 = load i8, i8* %rhs, align 1, !tbaa !6
  %cmp523 = icmp eq i8 %1, 0
  br i1 %cmp523, label %for.end11, label %for.body7

for.body:                                         ; preds = %entry, %for.body
  %2 = phi i8 [ %3, %for.body ], [ %0, %entry ]
  %ptr.028 = phi i8* [ %incdec.ptr2, %for.body ], [ %call, %entry ]
  %lhs.addr.027 = phi i8* [ %incdec.ptr, %for.body ], [ %lhs, %entry ]
  store i8 %2, i8* %ptr.028, align 1, !tbaa !6
  %incdec.ptr = getelementptr inbounds i8, i8* %lhs.addr.027, i64 1
  %incdec.ptr2 = getelementptr inbounds i8, i8* %ptr.028, i64 1
  %3 = load i8, i8* %incdec.ptr, align 1, !tbaa !6
  %cmp = icmp eq i8 %3, 0
  br i1 %cmp, label %for.cond3.preheader, label %for.body

for.body7:                                        ; preds = %for.cond3.preheader, %for.body7
  %4 = phi i8 [ %5, %for.body7 ], [ %1, %for.cond3.preheader ]
  %ptr.125 = phi i8* [ %incdec.ptr10, %for.body7 ], [ %ptr.0.lcssa, %for.cond3.preheader ]
  %rhs.addr.024 = phi i8* [ %incdec.ptr9, %for.body7 ], [ %rhs, %for.cond3.preheader ]
  store i8 %4, i8* %ptr.125, align 1, !tbaa !6
  %incdec.ptr9 = getelementptr inbounds i8, i8* %rhs.addr.024, i64 1
  %incdec.ptr10 = getelementptr inbounds i8, i8* %ptr.125, i64 1
  %5 = load i8, i8* %incdec.ptr9, align 1, !tbaa !6
  %cmp5 = icmp eq i8 %5, 0
  br i1 %cmp5, label %for.end11, label %for.body7

for.end11:                                        ; preds = %for.body7, %for.cond3.preheader
  %ptr.1.lcssa = phi i8* [ %ptr.0.lcssa, %for.cond3.preheader ], [ %incdec.ptr10, %for.body7 ]
  store i8 0, i8* %ptr.1.lcssa, align 1, !tbaa !6
  ret i8* %call
}

; Function Attrs: norecurse nounwind readonly uwtable
define dso_local zeroext i1 @builtin.string.isEqual(i8* nocapture readonly %lhs, i8* nocapture readonly %rhs) local_unnamed_addr #4 {
entry:
  %0 = load i8, i8* %lhs, align 1, !tbaa !6
  %1 = load i8, i8* %rhs, align 1, !tbaa !6
  %2 = or i8 %0, %1
  %3 = icmp eq i8 %2, 0
  br i1 %3, label %return, label %for.body

for.body:                                         ; preds = %entry, %for.inc
  %4 = phi i8 [ %7, %for.inc ], [ %1, %entry ]
  %5 = phi i8 [ %6, %for.inc ], [ %0, %entry ]
  %rhs.addr.015 = phi i8* [ %incdec.ptr9, %for.inc ], [ %rhs, %entry ]
  %lhs.addr.014 = phi i8* [ %incdec.ptr, %for.inc ], [ %lhs, %entry ]
  %cmp7 = icmp eq i8 %5, %4
  br i1 %cmp7, label %for.inc, label %return

for.inc:                                          ; preds = %for.body
  %incdec.ptr = getelementptr inbounds i8, i8* %lhs.addr.014, i64 1
  %incdec.ptr9 = getelementptr inbounds i8, i8* %rhs.addr.015, i64 1
  %6 = load i8, i8* %incdec.ptr, align 1, !tbaa !6
  %7 = load i8, i8* %incdec.ptr9, align 1, !tbaa !6
  %8 = or i8 %6, %7
  %9 = icmp eq i8 %8, 0
  br i1 %9, label %return, label %for.body

return:                                           ; preds = %for.body, %for.inc, %entry
  %retval.0 = phi i1 [ true, %entry ], [ false, %for.body ], [ true, %for.inc ]
  ret i1 %retval.0
}

; Function Attrs: norecurse nounwind readonly uwtable
define dso_local zeroext i1 @builtin.string.isNotEqual(i8* nocapture readonly %lhs, i8* nocapture readonly %rhs) local_unnamed_addr #4 {
entry:
  %0 = load i8, i8* %lhs, align 1, !tbaa !6
  %1 = load i8, i8* %rhs, align 1, !tbaa !6
  %2 = or i8 %0, %1
  %3 = icmp eq i8 %2, 0
  br i1 %3, label %return, label %for.body

for.body:                                         ; preds = %entry, %for.inc
  %4 = phi i8 [ %7, %for.inc ], [ %1, %entry ]
  %5 = phi i8 [ %6, %for.inc ], [ %0, %entry ]
  %rhs.addr.015 = phi i8* [ %incdec.ptr9, %for.inc ], [ %rhs, %entry ]
  %lhs.addr.014 = phi i8* [ %incdec.ptr, %for.inc ], [ %lhs, %entry ]
  %cmp7 = icmp eq i8 %5, %4
  br i1 %cmp7, label %for.inc, label %return

for.inc:                                          ; preds = %for.body
  %incdec.ptr = getelementptr inbounds i8, i8* %lhs.addr.014, i64 1
  %incdec.ptr9 = getelementptr inbounds i8, i8* %rhs.addr.015, i64 1
  %6 = load i8, i8* %incdec.ptr, align 1, !tbaa !6
  %7 = load i8, i8* %incdec.ptr9, align 1, !tbaa !6
  %8 = or i8 %6, %7
  %9 = icmp eq i8 %8, 0
  br i1 %9, label %return, label %for.body

return:                                           ; preds = %for.body, %for.inc, %entry
  %retval.0 = phi i1 [ false, %entry ], [ true, %for.body ], [ false, %for.inc ]
  ret i1 %retval.0
}

; Function Attrs: norecurse nounwind readonly uwtable
define dso_local zeroext i1 @builtin.string.isLessThan(i8* nocapture readonly %lhs, i8* nocapture readonly %rhs) local_unnamed_addr #4 {
entry:
  %0 = load i8, i8* %lhs, align 1, !tbaa !6
  %1 = load i8, i8* %rhs, align 1, !tbaa !6
  %2 = or i8 %0, %1
  %3 = icmp eq i8 %2, 0
  br i1 %3, label %return, label %for.body

for.body:                                         ; preds = %entry, %for.inc
  %4 = phi i8 [ %7, %for.inc ], [ %1, %entry ]
  %5 = phi i8 [ %6, %for.inc ], [ %0, %entry ]
  %rhs.addr.023 = phi i8* [ %incdec.ptr15, %for.inc ], [ %rhs, %entry ]
  %lhs.addr.022 = phi i8* [ %incdec.ptr, %for.inc ], [ %lhs, %entry ]
  %cmp7 = icmp slt i8 %5, %4
  br i1 %cmp7, label %return, label %if.end

if.end:                                           ; preds = %for.body
  %cmp11 = icmp sgt i8 %5, %4
  br i1 %cmp11, label %return, label %for.inc

for.inc:                                          ; preds = %if.end
  %incdec.ptr = getelementptr inbounds i8, i8* %lhs.addr.022, i64 1
  %incdec.ptr15 = getelementptr inbounds i8, i8* %rhs.addr.023, i64 1
  %6 = load i8, i8* %incdec.ptr, align 1, !tbaa !6
  %7 = load i8, i8* %incdec.ptr15, align 1, !tbaa !6
  %8 = or i8 %6, %7
  %9 = icmp eq i8 %8, 0
  br i1 %9, label %return, label %for.body

return:                                           ; preds = %for.body, %if.end, %for.inc, %entry
  %retval.0 = phi i1 [ false, %entry ], [ true, %for.body ], [ false, %if.end ], [ false, %for.inc ]
  ret i1 %retval.0
}

; Function Attrs: norecurse nounwind readonly uwtable
define dso_local zeroext i1 @builtin.string.isGreaterThan(i8* nocapture readonly %lhs, i8* nocapture readonly %rhs) local_unnamed_addr #4 {
entry:
  %0 = load i8, i8* %lhs, align 1, !tbaa !6
  %1 = load i8, i8* %rhs, align 1, !tbaa !6
  %2 = or i8 %0, %1
  %3 = icmp eq i8 %2, 0
  br i1 %3, label %return, label %for.body

for.body:                                         ; preds = %entry, %for.inc
  %4 = phi i8 [ %7, %for.inc ], [ %1, %entry ]
  %5 = phi i8 [ %6, %for.inc ], [ %0, %entry ]
  %rhs.addr.023 = phi i8* [ %incdec.ptr15, %for.inc ], [ %rhs, %entry ]
  %lhs.addr.022 = phi i8* [ %incdec.ptr, %for.inc ], [ %lhs, %entry ]
  %cmp7 = icmp sgt i8 %5, %4
  br i1 %cmp7, label %return, label %if.end

if.end:                                           ; preds = %for.body
  %cmp11 = icmp slt i8 %5, %4
  br i1 %cmp11, label %return, label %for.inc

for.inc:                                          ; preds = %if.end
  %incdec.ptr = getelementptr inbounds i8, i8* %lhs.addr.022, i64 1
  %incdec.ptr15 = getelementptr inbounds i8, i8* %rhs.addr.023, i64 1
  %6 = load i8, i8* %incdec.ptr, align 1, !tbaa !6
  %7 = load i8, i8* %incdec.ptr15, align 1, !tbaa !6
  %8 = or i8 %6, %7
  %9 = icmp eq i8 %8, 0
  br i1 %9, label %return, label %for.body

return:                                           ; preds = %for.body, %if.end, %for.inc, %entry
  %retval.0 = phi i1 [ false, %entry ], [ true, %for.body ], [ false, %if.end ], [ false, %for.inc ]
  ret i1 %retval.0
}

; Function Attrs: norecurse nounwind readonly uwtable
define dso_local zeroext i1 @builtin.string.isLessThanOrEqual(i8* nocapture readonly %lhs, i8* nocapture readonly %rhs) local_unnamed_addr #4 {
entry:
  %0 = load i8, i8* %lhs, align 1, !tbaa !6
  %1 = load i8, i8* %rhs, align 1, !tbaa !6
  %2 = or i8 %0, %1
  %3 = icmp eq i8 %2, 0
  %cmp723 = icmp slt i8 %0, %1
  %or.cond2224 = or i1 %3, %cmp723
  br i1 %or.cond2224, label %return, label %if.end

if.end:                                           ; preds = %entry, %for.inc
  %4 = phi i8 [ %7, %for.inc ], [ %1, %entry ]
  %5 = phi i8 [ %6, %for.inc ], [ %0, %entry ]
  %rhs.addr.026 = phi i8* [ %incdec.ptr15, %for.inc ], [ %rhs, %entry ]
  %lhs.addr.025 = phi i8* [ %incdec.ptr, %for.inc ], [ %lhs, %entry ]
  %cmp11 = icmp sgt i8 %5, %4
  br i1 %cmp11, label %return, label %for.inc

for.inc:                                          ; preds = %if.end
  %incdec.ptr = getelementptr inbounds i8, i8* %lhs.addr.025, i64 1
  %incdec.ptr15 = getelementptr inbounds i8, i8* %rhs.addr.026, i64 1
  %6 = load i8, i8* %incdec.ptr, align 1, !tbaa !6
  %7 = load i8, i8* %incdec.ptr15, align 1, !tbaa !6
  %8 = or i8 %6, %7
  %9 = icmp eq i8 %8, 0
  %cmp7 = icmp slt i8 %6, %7
  %or.cond22 = or i1 %9, %cmp7
  br i1 %or.cond22, label %return, label %if.end

return:                                           ; preds = %if.end, %for.inc, %entry
  %retval.0 = phi i1 [ true, %entry ], [ false, %if.end ], [ true, %for.inc ]
  ret i1 %retval.0
}

; Function Attrs: norecurse nounwind readonly uwtable
define dso_local zeroext i1 @builtin.string.isGreaterThanOrEqual(i8* nocapture readonly %lhs, i8* nocapture readonly %rhs) local_unnamed_addr #4 {
entry:
  %0 = load i8, i8* %lhs, align 1, !tbaa !6
  %1 = load i8, i8* %rhs, align 1, !tbaa !6
  %2 = or i8 %0, %1
  %3 = icmp eq i8 %2, 0
  %cmp723 = icmp sgt i8 %0, %1
  %or.cond2224 = or i1 %3, %cmp723
  br i1 %or.cond2224, label %return, label %if.end

if.end:                                           ; preds = %entry, %for.inc
  %4 = phi i8 [ %7, %for.inc ], [ %1, %entry ]
  %5 = phi i8 [ %6, %for.inc ], [ %0, %entry ]
  %rhs.addr.026 = phi i8* [ %incdec.ptr15, %for.inc ], [ %rhs, %entry ]
  %lhs.addr.025 = phi i8* [ %incdec.ptr, %for.inc ], [ %lhs, %entry ]
  %cmp11 = icmp slt i8 %5, %4
  br i1 %cmp11, label %return, label %for.inc

for.inc:                                          ; preds = %if.end
  %incdec.ptr = getelementptr inbounds i8, i8* %lhs.addr.025, i64 1
  %incdec.ptr15 = getelementptr inbounds i8, i8* %rhs.addr.026, i64 1
  %6 = load i8, i8* %incdec.ptr, align 1, !tbaa !6
  %7 = load i8, i8* %incdec.ptr15, align 1, !tbaa !6
  %8 = or i8 %6, %7
  %9 = icmp eq i8 %8, 0
  %cmp7 = icmp sgt i8 %6, %7
  %or.cond22 = or i1 %9, %cmp7
  br i1 %or.cond22, label %return, label %if.end

return:                                           ; preds = %if.end, %for.inc, %entry
  %retval.0 = phi i1 [ true, %entry ], [ false, %if.end ], [ true, %for.inc ]
  ret i1 %retval.0
}

; Function Attrs: norecurse nounwind readonly uwtable
define dso_local i32 @builtin.string.length(i8* nocapture readonly %s) local_unnamed_addr #4 {
entry:
  %0 = load i8, i8* %s, align 1, !tbaa !6
  %cmp5 = icmp eq i8 %0, 0
  br i1 %cmp5, label %for.end, label %for.inc

for.inc:                                          ; preds = %entry, %for.inc
  %len.07 = phi i32 [ %inc, %for.inc ], [ 0, %entry ]
  %s.addr.06 = phi i8* [ %incdec.ptr, %for.inc ], [ %s, %entry ]
  %incdec.ptr = getelementptr inbounds i8, i8* %s.addr.06, i64 1
  %inc = add nuw nsw i32 %len.07, 1
  %1 = load i8, i8* %incdec.ptr, align 1, !tbaa !6
  %cmp = icmp eq i8 %1, 0
  br i1 %cmp, label %for.end, label %for.inc

for.end:                                          ; preds = %for.inc, %entry
  %len.0.lcssa = phi i32 [ 0, %entry ], [ %inc, %for.inc ]
  ret i32 %len.0.lcssa
}

; Function Attrs: nofree nounwind uwtable
define dso_local noalias i8* @builtin.string.substring(i8* nocapture readonly %s, i32 %l, i32 %r) local_unnamed_addr #0 {
entry:
  %call = tail call noalias dereferenceable_or_null(1024) i8* @malloc(i64 1024) #8
  %tobool26 = icmp eq i32 %l, 0
  %0 = add i32 %l, -1
  %1 = zext i32 %0 to i64
  %2 = add nuw nsw i64 %1, 1
  %scevgep = getelementptr i8, i8* %s, i64 %2
  %3 = sub i32 %r, %l
  %s.addr.0.lcssa = select i1 %tobool26, i8* %s, i8* %scevgep
  %tobool322 = icmp eq i32 %3, 0
  br i1 %tobool322, label %for.end8, label %for.body4.preheader

for.body4.preheader:                              ; preds = %entry
  %4 = xor i32 %l, -1
  %5 = add i32 %4, %r
  %6 = zext i32 %5 to i64
  %7 = add nuw nsw i64 %6, 1
  %min.iters.check = icmp ult i32 %5, 31
  br i1 %min.iters.check, label %for.body4.preheader44, label %vector.memcheck

vector.memcheck:                                  ; preds = %for.body4.preheader
  %8 = xor i32 %l, -1
  %9 = add i32 %8, %r
  %10 = zext i32 %9 to i64
  %11 = add nuw nsw i64 %10, 1
  %scevgep32 = getelementptr i8, i8* %call, i64 %11
  %scevgep33 = getelementptr i8, i8* %s.addr.0.lcssa, i64 %11
  %bound0 = icmp ult i8* %call, %scevgep33
  %bound1 = icmp ult i8* %s.addr.0.lcssa, %scevgep32
  %found.conflict = and i1 %bound0, %bound1
  br i1 %found.conflict, label %for.body4.preheader44, label %vector.ph

vector.ph:                                        ; preds = %vector.memcheck
  %n.vec = and i64 %7, 8589934560
  %ind.end = getelementptr i8, i8* %s.addr.0.lcssa, i64 %n.vec
  %cast.crd = trunc i64 %n.vec to i32
  %ind.end36 = sub i32 %3, %cast.crd
  %12 = add nsw i64 %n.vec, -32
  %13 = lshr exact i64 %12, 5
  %14 = add nuw nsw i64 %13, 1
  %xtraiter46 = and i64 %14, 3
  %15 = icmp ult i64 %12, 96
  br i1 %15, label %middle.block.unr-lcssa, label %vector.ph.new

vector.ph.new:                                    ; preds = %vector.ph
  %unroll_iter = sub nsw i64 %14, %xtraiter46
  br label %vector.body

vector.body:                                      ; preds = %vector.body, %vector.ph.new
  %index = phi i64 [ 0, %vector.ph.new ], [ %index.next.3, %vector.body ]
  %niter = phi i64 [ %unroll_iter, %vector.ph.new ], [ %niter.nsub.3, %vector.body ]
  %next.gep = getelementptr i8, i8* %s.addr.0.lcssa, i64 %index
  %16 = bitcast i8* %next.gep to <16 x i8>*
  %wide.load = load <16 x i8>, <16 x i8>* %16, align 1, !tbaa !6, !alias.scope !7
  %17 = getelementptr i8, i8* %next.gep, i64 16
  %18 = bitcast i8* %17 to <16 x i8>*
  %wide.load43 = load <16 x i8>, <16 x i8>* %18, align 1, !tbaa !6, !alias.scope !7
  %19 = getelementptr inbounds i8, i8* %call, i64 %index
  %20 = bitcast i8* %19 to <16 x i8>*
  store <16 x i8> %wide.load, <16 x i8>* %20, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %21 = getelementptr inbounds i8, i8* %19, i64 16
  %22 = bitcast i8* %21 to <16 x i8>*
  store <16 x i8> %wide.load43, <16 x i8>* %22, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %index.next = or i64 %index, 32
  %next.gep.1 = getelementptr i8, i8* %s.addr.0.lcssa, i64 %index.next
  %23 = bitcast i8* %next.gep.1 to <16 x i8>*
  %wide.load.1 = load <16 x i8>, <16 x i8>* %23, align 1, !tbaa !6, !alias.scope !7
  %24 = getelementptr i8, i8* %next.gep.1, i64 16
  %25 = bitcast i8* %24 to <16 x i8>*
  %wide.load43.1 = load <16 x i8>, <16 x i8>* %25, align 1, !tbaa !6, !alias.scope !7
  %26 = getelementptr inbounds i8, i8* %call, i64 %index.next
  %27 = bitcast i8* %26 to <16 x i8>*
  store <16 x i8> %wide.load.1, <16 x i8>* %27, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %28 = getelementptr inbounds i8, i8* %26, i64 16
  %29 = bitcast i8* %28 to <16 x i8>*
  store <16 x i8> %wide.load43.1, <16 x i8>* %29, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %index.next.1 = or i64 %index, 64
  %next.gep.2 = getelementptr i8, i8* %s.addr.0.lcssa, i64 %index.next.1
  %30 = bitcast i8* %next.gep.2 to <16 x i8>*
  %wide.load.2 = load <16 x i8>, <16 x i8>* %30, align 1, !tbaa !6, !alias.scope !7
  %31 = getelementptr i8, i8* %next.gep.2, i64 16
  %32 = bitcast i8* %31 to <16 x i8>*
  %wide.load43.2 = load <16 x i8>, <16 x i8>* %32, align 1, !tbaa !6, !alias.scope !7
  %33 = getelementptr inbounds i8, i8* %call, i64 %index.next.1
  %34 = bitcast i8* %33 to <16 x i8>*
  store <16 x i8> %wide.load.2, <16 x i8>* %34, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %35 = getelementptr inbounds i8, i8* %33, i64 16
  %36 = bitcast i8* %35 to <16 x i8>*
  store <16 x i8> %wide.load43.2, <16 x i8>* %36, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %index.next.2 = or i64 %index, 96
  %next.gep.3 = getelementptr i8, i8* %s.addr.0.lcssa, i64 %index.next.2
  %37 = bitcast i8* %next.gep.3 to <16 x i8>*
  %wide.load.3 = load <16 x i8>, <16 x i8>* %37, align 1, !tbaa !6, !alias.scope !7
  %38 = getelementptr i8, i8* %next.gep.3, i64 16
  %39 = bitcast i8* %38 to <16 x i8>*
  %wide.load43.3 = load <16 x i8>, <16 x i8>* %39, align 1, !tbaa !6, !alias.scope !7
  %40 = getelementptr inbounds i8, i8* %call, i64 %index.next.2
  %41 = bitcast i8* %40 to <16 x i8>*
  store <16 x i8> %wide.load.3, <16 x i8>* %41, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %42 = getelementptr inbounds i8, i8* %40, i64 16
  %43 = bitcast i8* %42 to <16 x i8>*
  store <16 x i8> %wide.load43.3, <16 x i8>* %43, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %index.next.3 = add i64 %index, 128
  %niter.nsub.3 = add i64 %niter, -4
  %niter.ncmp.3 = icmp eq i64 %niter.nsub.3, 0
  br i1 %niter.ncmp.3, label %middle.block.unr-lcssa, label %vector.body, !llvm.loop !12

middle.block.unr-lcssa:                           ; preds = %vector.body, %vector.ph
  %index.unr = phi i64 [ 0, %vector.ph ], [ %index.next.3, %vector.body ]
  %lcmp.mod47 = icmp eq i64 %xtraiter46, 0
  br i1 %lcmp.mod47, label %middle.block, label %vector.body.epil

vector.body.epil:                                 ; preds = %middle.block.unr-lcssa, %vector.body.epil
  %index.epil = phi i64 [ %index.next.epil, %vector.body.epil ], [ %index.unr, %middle.block.unr-lcssa ]
  %epil.iter = phi i64 [ %epil.iter.sub, %vector.body.epil ], [ %xtraiter46, %middle.block.unr-lcssa ]
  %next.gep.epil = getelementptr i8, i8* %s.addr.0.lcssa, i64 %index.epil
  %44 = bitcast i8* %next.gep.epil to <16 x i8>*
  %wide.load.epil = load <16 x i8>, <16 x i8>* %44, align 1, !tbaa !6, !alias.scope !7
  %45 = getelementptr i8, i8* %next.gep.epil, i64 16
  %46 = bitcast i8* %45 to <16 x i8>*
  %wide.load43.epil = load <16 x i8>, <16 x i8>* %46, align 1, !tbaa !6, !alias.scope !7
  %47 = getelementptr inbounds i8, i8* %call, i64 %index.epil
  %48 = bitcast i8* %47 to <16 x i8>*
  store <16 x i8> %wide.load.epil, <16 x i8>* %48, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %49 = getelementptr inbounds i8, i8* %47, i64 16
  %50 = bitcast i8* %49 to <16 x i8>*
  store <16 x i8> %wide.load43.epil, <16 x i8>* %50, align 1, !tbaa !6, !alias.scope !10, !noalias !7
  %index.next.epil = add i64 %index.epil, 32
  %epil.iter.sub = add i64 %epil.iter, -1
  %epil.iter.cmp = icmp eq i64 %epil.iter.sub, 0
  br i1 %epil.iter.cmp, label %middle.block, label %vector.body.epil, !llvm.loop !14

middle.block:                                     ; preds = %vector.body.epil, %middle.block.unr-lcssa
  %cmp.n = icmp eq i64 %7, %n.vec
  br i1 %cmp.n, label %for.end8.loopexit, label %for.body4.preheader44

for.body4.preheader44:                            ; preds = %middle.block, %vector.memcheck, %for.body4.preheader
  %indvars.iv.ph = phi i64 [ 0, %vector.memcheck ], [ 0, %for.body4.preheader ], [ %n.vec, %middle.block ]
  %s.addr.125.ph = phi i8* [ %s.addr.0.lcssa, %vector.memcheck ], [ %s.addr.0.lcssa, %for.body4.preheader ], [ %ind.end, %middle.block ]
  %r.addr.124.ph = phi i32 [ %3, %vector.memcheck ], [ %3, %for.body4.preheader ], [ %ind.end36, %middle.block ]
  %51 = add i32 %r.addr.124.ph, -1
  %xtraiter = and i32 %r.addr.124.ph, 3
  %lcmp.mod = icmp eq i32 %xtraiter, 0
  br i1 %lcmp.mod, label %for.body4.prol.loopexit, label %for.body4.prol

for.body4.prol:                                   ; preds = %for.body4.preheader44, %for.body4.prol
  %indvars.iv.prol = phi i64 [ %indvars.iv.next.prol, %for.body4.prol ], [ %indvars.iv.ph, %for.body4.preheader44 ]
  %s.addr.125.prol = phi i8* [ %incdec.ptr6.prol, %for.body4.prol ], [ %s.addr.125.ph, %for.body4.preheader44 ]
  %r.addr.124.prol = phi i32 [ %dec7.prol, %for.body4.prol ], [ %r.addr.124.ph, %for.body4.preheader44 ]
  %prol.iter = phi i32 [ %prol.iter.sub, %for.body4.prol ], [ %xtraiter, %for.body4.preheader44 ]
  %52 = load i8, i8* %s.addr.125.prol, align 1, !tbaa !6
  %arrayidx.prol = getelementptr inbounds i8, i8* %call, i64 %indvars.iv.prol
  store i8 %52, i8* %arrayidx.prol, align 1, !tbaa !6
  %incdec.ptr6.prol = getelementptr inbounds i8, i8* %s.addr.125.prol, i64 1
  %dec7.prol = add nsw i32 %r.addr.124.prol, -1
  %indvars.iv.next.prol = add nuw nsw i64 %indvars.iv.prol, 1
  %prol.iter.sub = add i32 %prol.iter, -1
  %prol.iter.cmp = icmp eq i32 %prol.iter.sub, 0
  br i1 %prol.iter.cmp, label %for.body4.prol.loopexit, label %for.body4.prol, !llvm.loop !16

for.body4.prol.loopexit:                          ; preds = %for.body4.prol, %for.body4.preheader44
  %indvars.iv.next.lcssa45.unr = phi i64 [ undef, %for.body4.preheader44 ], [ %indvars.iv.next.prol, %for.body4.prol ]
  %indvars.iv.unr = phi i64 [ %indvars.iv.ph, %for.body4.preheader44 ], [ %indvars.iv.next.prol, %for.body4.prol ]
  %s.addr.125.unr = phi i8* [ %s.addr.125.ph, %for.body4.preheader44 ], [ %incdec.ptr6.prol, %for.body4.prol ]
  %r.addr.124.unr = phi i32 [ %r.addr.124.ph, %for.body4.preheader44 ], [ %dec7.prol, %for.body4.prol ]
  %53 = icmp ult i32 %51, 3
  br i1 %53, label %for.end8.loopexit, label %for.body4

for.body4:                                        ; preds = %for.body4.prol.loopexit, %for.body4
  %indvars.iv = phi i64 [ %indvars.iv.next.3, %for.body4 ], [ %indvars.iv.unr, %for.body4.prol.loopexit ]
  %s.addr.125 = phi i8* [ %incdec.ptr6.3, %for.body4 ], [ %s.addr.125.unr, %for.body4.prol.loopexit ]
  %r.addr.124 = phi i32 [ %dec7.3, %for.body4 ], [ %r.addr.124.unr, %for.body4.prol.loopexit ]
  %54 = load i8, i8* %s.addr.125, align 1, !tbaa !6
  %arrayidx = getelementptr inbounds i8, i8* %call, i64 %indvars.iv
  store i8 %54, i8* %arrayidx, align 1, !tbaa !6
  %incdec.ptr6 = getelementptr inbounds i8, i8* %s.addr.125, i64 1
  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1
  %55 = load i8, i8* %incdec.ptr6, align 1, !tbaa !6
  %arrayidx.1 = getelementptr inbounds i8, i8* %call, i64 %indvars.iv.next
  store i8 %55, i8* %arrayidx.1, align 1, !tbaa !6
  %incdec.ptr6.1 = getelementptr inbounds i8, i8* %s.addr.125, i64 2
  %indvars.iv.next.1 = add nuw nsw i64 %indvars.iv, 2
  %56 = load i8, i8* %incdec.ptr6.1, align 1, !tbaa !6
  %arrayidx.2 = getelementptr inbounds i8, i8* %call, i64 %indvars.iv.next.1
  store i8 %56, i8* %arrayidx.2, align 1, !tbaa !6
  %incdec.ptr6.2 = getelementptr inbounds i8, i8* %s.addr.125, i64 3
  %indvars.iv.next.2 = add nuw nsw i64 %indvars.iv, 3
  %57 = load i8, i8* %incdec.ptr6.2, align 1, !tbaa !6
  %arrayidx.3 = getelementptr inbounds i8, i8* %call, i64 %indvars.iv.next.2
  store i8 %57, i8* %arrayidx.3, align 1, !tbaa !6
  %incdec.ptr6.3 = getelementptr inbounds i8, i8* %s.addr.125, i64 4
  %dec7.3 = add nsw i32 %r.addr.124, -4
  %indvars.iv.next.3 = add nuw nsw i64 %indvars.iv, 4
  %tobool3.3 = icmp eq i32 %dec7.3, 0
  br i1 %tobool3.3, label %for.end8.loopexit, label %for.body4, !llvm.loop !17

for.end8.loopexit:                                ; preds = %for.body4.prol.loopexit, %for.body4, %middle.block
  %indvars.iv.next.lcssa = phi i64 [ %n.vec, %middle.block ], [ %indvars.iv.next.lcssa45.unr, %for.body4.prol.loopexit ], [ %indvars.iv.next.3, %for.body4 ]
  %phitmp = and i64 %indvars.iv.next.lcssa, 4294967295
  br label %for.end8

for.end8:                                         ; preds = %for.end8.loopexit, %entry
  %l.addr.1.lcssa = phi i64 [ 0, %entry ], [ %phitmp, %for.end8.loopexit ]
  %arrayidx10 = getelementptr inbounds i8, i8* %call, i64 %l.addr.1.lcssa
  store i8 0, i8* %arrayidx10, align 1, !tbaa !6
  ret i8* %call
}

; Function Attrs: nounwind readonly uwtable
define dso_local i32 @builtin.string.parseInt(i8* nocapture readonly %s) local_unnamed_addr #5 {
entry:
  %call = tail call i16** @__ctype_b_loc() #9
  %0 = load i16*, i16** %call, align 8, !tbaa !18
  %1 = load i8, i8* %s, align 1, !tbaa !6
  %idxprom6 = sext i8 %1 to i64
  %arrayidx7 = getelementptr inbounds i16, i16* %0, i64 %idxprom6
  %2 = load i16, i16* %arrayidx7, align 2, !tbaa !20
  %3 = and i16 %2, 2048
  %tobool8 = icmp eq i16 %3, 0
  br i1 %tobool8, label %for.end, label %for.body

for.body:                                         ; preds = %entry, %for.body
  %4 = phi i8 [ %5, %for.body ], [ %1, %entry ]
  %ret.010 = phi i32 [ %add, %for.body ], [ 0, %entry ]
  %s.addr.09 = phi i8* [ %incdec.ptr, %for.body ], [ %s, %entry ]
  %conv = sext i8 %4 to i32
  %mul = mul nsw i32 %ret.010, 10
  %sub = add i32 %mul, -48
  %add = add i32 %sub, %conv
  %incdec.ptr = getelementptr inbounds i8, i8* %s.addr.09, i64 1
  %5 = load i8, i8* %incdec.ptr, align 1, !tbaa !6
  %idxprom = sext i8 %5 to i64
  %arrayidx = getelementptr inbounds i16, i16* %0, i64 %idxprom
  %6 = load i16, i16* %arrayidx, align 2, !tbaa !20
  %7 = and i16 %6, 2048
  %tobool = icmp eq i16 %7, 0
  br i1 %tobool, label %for.end, label %for.body

for.end:                                          ; preds = %for.body, %entry
  %ret.0.lcssa = phi i32 [ 0, %entry ], [ %add, %for.body ]
  ret i32 %ret.0.lcssa
}

; Function Attrs: nounwind readnone
declare dso_local i16** @__ctype_b_loc() local_unnamed_addr #6

; Function Attrs: norecurse nounwind readonly uwtable
define dso_local i32 @builtin.string.ord(i8* nocapture readonly %s, i32 %pos) local_unnamed_addr #4 {
entry:
  %tobool2 = icmp eq i32 %pos, 0
  %0 = add i32 %pos, -1
  %1 = zext i32 %0 to i64
  %2 = add nuw nsw i64 %1, 1
  %scevgep = getelementptr i8, i8* %s, i64 %2
  %s.addr.0.lcssa = select i1 %tobool2, i8* %s, i8* %scevgep
  %3 = load i8, i8* %s.addr.0.lcssa, align 1, !tbaa !6
  %conv = sext i8 %3 to i32
  ret i32 %conv
}

; Function Attrs: nofree nounwind
declare i32 @puts(i8* nocapture readonly) local_unnamed_addr #7

attributes #0 = { nofree nounwind uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "frame-pointer"="none" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #1 = { nofree nounwind "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "frame-pointer"="none" "less-precise-fpmad"="false" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #2 = { argmemonly nounwind willreturn }
attributes #3 = { nounwind uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "frame-pointer"="none" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #4 = { norecurse nounwind readonly uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "frame-pointer"="none" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #5 = { nounwind readonly uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "frame-pointer"="none" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #6 = { nounwind readnone "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "frame-pointer"="none" "less-precise-fpmad"="false" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #7 = { nofree nounwind }
attributes #8 = { nounwind }
attributes #9 = { nounwind readnone }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 10.0.0-4ubuntu1~18.04.2 "}
!2 = !{!3, !3, i64 0}
!3 = !{!"int", !4, i64 0}
!4 = !{!"omnipotent char", !5, i64 0}
!5 = !{!"Simple C/C++ TBAA"}
!6 = !{!4, !4, i64 0}
!7 = !{!8}
!8 = distinct !{!8, !9}
!9 = distinct !{!9, !"LVerDomain"}
!10 = !{!11}
!11 = distinct !{!11, !9}
!12 = distinct !{!12, !13}
!13 = !{!"llvm.loop.isvectorized", i32 1}
!14 = distinct !{!14, !15}
!15 = !{!"llvm.loop.unroll.disable"}
!16 = distinct !{!16, !15}
!17 = distinct !{!17, !13}
!18 = !{!19, !19, i64 0}
!19 = !{!"any pointer", !4, i64 0}
!20 = !{!21, !21, i64 0}
!21 = !{!"short", !4, i64 0}
