if [ -z "$1" ]
  then
    echo "No arguments supplied"
  else
     java -jar target/surface-1.0-SNAPSHOT-jar-with-dependencies.jar -metrics CA,CM,CIVA,CCVA,CMA,CMR,CAI,CC,CCR,CCE,CME,CSCR,SCCR -remoteProjects "$1" -export csv
fi
