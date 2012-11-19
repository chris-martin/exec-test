
exec-test.war: webapp
	cp webapp/target/exec-test.war exec-test.war

webapp: executable
	cd webapp && make

executable:
	cd executable &&make
	mkdir -p webapp/src/main/resources/org/chris_martin/exectest
	cp executable/foo webapp/src/main/resources/org/chris_martin/exectest/foo

clean:
	rm -f exec-test.war
	rm -f webapp/src/main/resources/org/chris_martin/exectest/foo
	cd webapp && make clean
	cd executable && make clean

.PHONY: webapp executable clean
