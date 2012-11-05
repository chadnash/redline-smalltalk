import: 'java.util.Stack' as: 'JavaStack'.
Object<ChadJavaAdaptionExampleClass.st
- main
	|x|
	x :=  JavaStack new
 	x push: 1.
 	Transcript  cr; show: x pop
	Transcript  cr; show: 'KJJHKHJKL='.
	"Transcript  cr; show: 'KJJHKHJKL=' ; show: JavaStack new ; cr."

- add: newObject
    self shouldNotImplement.