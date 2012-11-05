

TestCase < #JavaAdaptingTest. "james if you forget the # you get a stack overflow"

JavaAdaptingTest import: 'java.util.ArrayList' as: 'JList'. " have to put this here  fro scoping reasons"

- test
    self testCreateArrayList.
    
- testCreateArrayList
    |l|
    l := JList new.
    self assert: l.size equals: 0 withMessage: 'JavaAdaptingTest testCreateArrayList failed'.


