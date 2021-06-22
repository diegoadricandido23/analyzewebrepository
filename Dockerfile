FROM vvcelparti01:443/alpine:3.12.0

COPY analyzewebrepository/target/analyzewebrepository.jar /tmp/analyzewebrepository.jar

RUN chown -vR 1002:1002 /tmp/analyzewebrepository.jar

FROM vvcelparti01:443/tools/rhel-jre11-wily:11.0.8

COPY --from=0 /tmp/analyzewebrepository.jar /app

CMD [ \
	"java", \
	"-jar", \
	"analyzewebrepository.jar" \
]
