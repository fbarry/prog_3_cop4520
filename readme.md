<p align="right">Fiona Barry<p>
<p align="right">04/13/2022<p>
<p align="right">COP4520<p>
<p align="right">Assignment 3<p>

# Minatour's Presents

## To compile and run code:

1. From Linux command line, navigate to `prog_3_cop4520/minatour` by typing the command `cd minatour`.
2. To compile all relevant files and run code, enter `javac Main.java && javac LockFreeList.java && javac Servant.java && java Main`.

## Evaluation

### Solution

The issue with the non-concurrent linked list is that more than one servant might write a "Thank you" note before removing the present. Therefore, you end up with more "Thank you" notes than presents.

The concurrent linked list solves this problem by protecting resources and ensuring that when updating, removing, or marking a "next" node, that node is the intended node to change in case another thread is removing or editing that node).

### Efficiency

The `add`, `pop`, and `contains` functions all operate in `O(n)`, where `n` is the number of presents. Ordinarily the run-time in the worst-case would be `O(n^2)`, but because all servants alternate between adding and removing, the linked list never grows to an `O(n)` length, and worst-case run-time is never achieved.

### Experimentation

To test this program, I ran it on smaller values of `n` with extensive output to ensure that every present was added and given a "Thank you" note and to validate the correctness of the `contains` function. Smaller numbers run much faster (obviously) and provide more insight than larger values.

# Mars Rover

## To compile and run code:

1. From Linux command line, navigate to `prog_3_cop4520/temperature` by typing the command `cd temperature`.
2. To compile all relevant files and run code, enter `make && java Rover`.
3. Output will be printed to a file called `reports.txt`.

_Note: If you make any changes to the files and wish to re-run, please enter `make clean` before restarting the process._

## Evaluation

### Solution

The solution is to create multiple `sensor` threads and a single `report` thread. The `sensor` threads incrementally populate a `readings` matrix, and the `report` thread uses that `readings` matrix to calcualte results. All sensors continue to cycle incrementally until they reach the pre-defined `TOTAL_TIME`.

### Efficiency

This program takes exactly the amount of time defined in the constants. The only potential concern would be that the `report` thread takes too long and would interrupt the `sensor` threads or cause a resource conflict. This is not an issue with this program because 1) the `report` thread is `O(fast)` (i.e. it loops through the shared resource exaclty once with `O(1)` functions) and 2) in the problem statement, the report would get a full minute to process.

### Experimentation

I tried a large and small number of reports to check values and printed debug (later deleted for code cleanliness).
