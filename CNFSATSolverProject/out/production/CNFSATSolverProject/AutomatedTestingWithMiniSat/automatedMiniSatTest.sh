#!/bin/bash
# My first script
MY_PATH=$(java AutomatedTestingWithMiniSat.AutomatedTestingFiles.GetPath "$1")
MY_VAR="ls ${MY_PATH}"
MY_VAR_TWO=$(eval "$MY_VAR")
IFS=$'\n' read -rd '' -a Y <<<"$MY_VAR_TWO"
for X in "${Y[@]}"
do
	MY_VAR1=$(java AutomatedTestingWithMiniSat.AutomatedTestingFiles.CheckIfAlreadySolved "$X")
	if [ "$MY_VAR1" = "false" ]
	then
		MINISAT=$(~/Documents/MiniSat/minisat/core/minisat "$X" output.txt)
		MINISAT_OUT=$(java AutomatedTestingWithMiniSat.AutomatedTestingFiles.GetResult "$MINISAT")
		MINISAT_TIME=$(java AutomatedTestingWithMiniSat.AutomatedTestingFiles.GetMiniSatCPUTime "$MINISAT")
		if [ "$MINISAT_OUT" = "SATISFIABLE" ]
		then
			java AutomatedTestingWithMiniSat.AutomatedTestingFiles.Satisfiable "$X" "$MINISAT_TIME" "./output.txt" 
		fi
		if [ "$MINISAT_OUT" = "UNSATISFIABLE" ]
		then
			java AutomatedTestingWithMiniSat.AutomatedTestingFiles.UnSatisfiable "$X" "$MINISAT_TIME"
		fi	
	fi	
done