Object < #GreetingAdaptor.

- sayHello
  Transcript show: 'Getting Greeting to say Hello.'; cr.
  JVM aload: 1.
  JVM new: 'com/friendly/Greeting'.
  JVM dup.
  JVM invokeSpecial: 'com/friendly/Greeting' method: '<init>' matching: '()V'.
  JVM invokeVirtual: 'com/friendly/Greeting' method: 'say' matching: '()V'.