"@: smallTalkClassesThatAdaptJavaClasses.st.redline.core.reflector.TestAdaptor"
Object < #TestAdaptor.
TestAdaptor class atSelector: #new put: [ | obj |
  obj := super new.
  JVM atTemp: 0;
      new: 'st/redline/core/reflector/Test';
      dup.
  JVM invokeSpecial: 'st/redline/core/reflector/Test' method: '<init>' matching: '()V';
      invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValue' matching: '(Ljava/lang/Object;)Lst/redline/core/PrimObject;'.
  ^ obj.
].

TestAdaptor class atSelector: #with: put: [ :args || selector |
  selector := self selectorFor: args withPrefix: 'with'.
  JVM aload: 1;
      arg: 0;
      atTemp: 0;
      invokeVirtual: 'st/redline/core/PrimObject' method: 'perform' matching: '(Lst/redline/core/PrimObject;Lst/redline/core/PrimObject;)Lst/redline/core/PrimObject;'.
].

TestAdaptor class atSelector: #newWithoutCallingAConstructor put: [^super new.
].

TestAdaptor class atSelector: #selectorFor:withPrefix: put: [ :args :prefix |
  <primitive: 227>
].
TestAdaptor atSelector: #equalsObject: put: [ :args || rtn |
  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM arg: 0 at: 1.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'equals' matching: '(Ljava/lang/Object;)Z'.
  JVM invokeStatic: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method:'smallTalkObjectForJavaBoolean' matching: '(Z)Lst/redline/core/PrimObject;';
      putTemp: 0.
  ^rtn.
].

TestAdaptor atSelector: #notify put: [   JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'notify' matching: '()V'.
].

TestAdaptor atSelector: #notifyAll put: [   JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'notifyAll' matching: '()V'.
].

TestAdaptor atSelector: #hashCode put: [ | rtn |
  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'hashCode' matching: '()I'.
  JVM invokeStatic: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method:'smallTalkObjectForJavaInt' matching: '(I)Lst/redline/core/PrimObject;';
      putTemp: 0.
  ^rtn.
].

TestAdaptor atSelector: #toString put: [ | rtn |
  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'toString' matching: '()Ljava/lang/String;'.
  JVM invokeStatic: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method:'smallTalkObjectForJavaValue' matching: '(Ljava/lang/Object;)Lst/redline/core/PrimObject;';
      putTemp: 0.
  ^rtn.
].

TestAdaptor atSelector: #waitJI: put: [ :args |  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM arg: 0 at: 1.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/math/BigDecimal';
      invokeVirtual: 'java/math/BigDecimal' method: 'longValue' matching: '()J'.
  JVM arg: 0 at: 2.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/math/BigDecimal';
      invokeVirtual: 'java/math/BigDecimal' method: 'intValue' matching: '()I'.
  JVM invokeVirtual: 'java/lang/Object' method: 'wait' matching: '(JI)V'.
].

TestAdaptor atSelector: #wait put: [   JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'wait' matching: '()V'.
].

TestAdaptor atSelector: #waitJ: put: [ :args |  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM arg: 0 at: 1.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/math/BigDecimal';
      invokeVirtual: 'java/math/BigDecimal' method: 'longValue' matching: '()J'.
  JVM invokeVirtual: 'java/lang/Object' method: 'wait' matching: '(J)V'.
].

TestAdaptor atSelector: #equals: put: [ :args |
^  self class performJavaMethodNamed:'equals' on:self WithArgs:args.
].


TestAdaptor atSelector: #wait: put: [ :args |
^  self class performJavaMethodNamed:'wait' on:self WithArgs:args.
].


self class atSelector: #performJavaMethodNamed:on:withArgs: put: [ :args || rtn |
  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass'.
  JVM arg: 0 at: 1.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM arg: 0 at: 2.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/String'.
  JVM arg: 0 at: 3.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: '[Ljava/lang/Object;'.
  JVM invokeVirtual: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method: 'invokeRightJavaMethod' matching: '(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;'.
  JVM invokeStatic: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method:'smallTalkObjectForJavaValue' matching: '(Ljava/lang/Object;)Lst/redline/core/PrimObject;';
      putTemp: 0.
  ^rtn.
].

TestAdaptor atSelector: #selectorFor:withPrefix: put: [ :args :prefix |
  <primitive: 227>
].

TestAdaptor initialize.

preproc
"@: smallTalkClassesThatAdaptJavaClasses.st.redline.core.reflector.TestAdaptor"
Object < #TestAdaptor.
TestAdaptor class atSelector: #new put: [ | obj |
  obj := super new.
  JVM atTemp: 0;
      new: 'st/redline/core/reflector/Test';
      dup.
  JVM invokeSpecial: 'st/redline/core/reflector/Test' method: '<init>' matching: '()V';
      invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValue' matching: '(Ljava/lang/Object;)Lst/redline/core/PrimObject;'.
  ^ obj.
].

TestAdaptor class atSelector: #with: put: [ :args || selector |
  selector := self selectorFor: args withPrefix: 'with'.
  JVM aload: 1;
      arg: 0;
      atTemp: 0;
      invokeVirtual: 'st/redline/core/PrimObject' method: 'perform' matching: '(Lst/redline/core/PrimObject;Lst/redline/core/PrimObject;)Lst/redline/core/PrimObject;'.
].

TestAdaptor class atSelector: #newWithoutCallingAConstructor put: [^super new.
].

TestAdaptor class atSelector: #selectorFor:withPrefix: put: [ :args :prefix |
  <primitive: 227>
].
TestAdaptor atSelector: #equalsObject: put: [ :args || rtn |
  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM arg: 0 at: 1.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'equals' matching: '(Ljava/lang/Object;)Z'.
  JVM invokeStatic: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method:'smallTalkObjectForJavaBoolean' matching: '(Z)Lst/redline/core/PrimObject;';
      putTemp: 0.
  ^rtn.
].

TestAdaptor atSelector: #notify put: [   JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'notify' matching: '()V'.
].

TestAdaptor atSelector: #notifyAll put: [   JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'notifyAll' matching: '()V'.
].

TestAdaptor atSelector: #hashCode put: [ | rtn |
  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'hashCode' matching: '()I'.
  JVM invokeStatic: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method:'smallTalkObjectForJavaInt' matching: '(I)Lst/redline/core/PrimObject;';
      putTemp: 0.
  ^rtn.
].

TestAdaptor atSelector: #toString put: [ | rtn |
  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'toString' matching: '()Ljava/lang/String;'.
  JVM invokeStatic: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method:'smallTalkObjectForJavaValue' matching: '(Ljava/lang/Object;)Lst/redline/core/PrimObject;';
      putTemp: 0.
  ^rtn.
].

TestAdaptor atSelector: #waitJI: put: [ :args |  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM arg: 0 at: 1.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/math/BigDecimal';
      invokeVirtual: 'java/math/BigDecimal' method: 'longValue' matching: '()J'.
  JVM arg: 0 at: 2.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/math/BigDecimal';
      invokeVirtual: 'java/math/BigDecimal' method: 'intValue' matching: '()I'.
  JVM invokeVirtual: 'java/lang/Object' method: 'wait' matching: '(JI)V'.
].

TestAdaptor atSelector: #wait put: [   JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM invokeVirtual: 'java/lang/Object' method: 'wait' matching: '()V'.
].

TestAdaptor atSelector: #waitJ: put: [ :args |  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM arg: 0 at: 1.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/math/BigDecimal';
      invokeVirtual: 'java/math/BigDecimal' method: 'longValue' matching: '()J'.
  JVM invokeVirtual: 'java/lang/Object' method: 'wait' matching: '(J)V'.
].

TestAdaptor atSelector: #equals: put: [ :args |
^  self class performJavaMethodNamed:'equals' on:self WithArgs:args.
].


TestAdaptor atSelector: #wait: put: [ :args |
^  self class performJavaMethodNamed:'wait' on:self WithArgs:args.
].


self class atSelector: #performJavaMethodNamed:on:withArgs: put: [ :args || rtn |
  JVM aload: 1; checkcast: 'st/redline/core/PrimObject'.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass'.
  JVM arg: 0 at: 1.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/Object'.
  JVM arg: 0 at: 2.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: 'java/lang/String'.
  JVM arg: 0 at: 3.
  JVM invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValueUndoingArrays' matching: '()Ljava/lang/Object;';
      checkcast: '[Ljava/lang/Object;'.
  JVM invokeVirtual: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method: 'invokeRightJavaMethod' matching: '(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;'.
  JVM invokeStatic: 'st/redline/core/SmallTalkGeneratorOfAdaptorOfAJavaClass' method:'smallTalkObjectForJavaValue' matching: '(Ljava/lang/Object;)Lst/redline/core/PrimObject;';
      putTemp: 0.
  ^rtn.
].

TestAdaptor atSelector: #selectorFor:withPrefix: put: [ :args :prefix |
  <primitive: 227>
].

TestAdaptor initialize.

