exec-test
=========

Demonstrates a webapp that copies an executable from its war,
sets the executable flag, and runs it.

Installation and usage
----------------------

Run ```make``` at the root directory. This generates ```exec-test.war```.

Copy ```exec-test.war``` into ```tomcat/webapps```.

Make an http request to ```localhost/exec-test```, and you should
see something like "```Foo: Mon Nov 19 17:38:58 GMT 2012```".

