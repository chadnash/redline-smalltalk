import: 'st.redline.XGreeting'.

Object < #XGreetingCaller.

-fails
	XGreeting new sayHello.
-works
	Transcript show: 'Getting Greeting to say Hello from works.'; cr.
	
	
