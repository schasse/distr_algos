# Distributed Algorithms - Exercise
Winter term 2017/18, Danh Le Phuoc & Qian Liu, ODS


## Exercise sheet 2


#### Exercise 1.1: Bully Algorithm

##### Questions

i. Describe the Bully Algorithm

The bully algorithm requires at least a unidirectional ring
topology. Every node wakes up either as an initiator or when it
receives a message. Those messages are the starts of circular chains
among all the nodes. Within a circular chain of messages the algorithm
determines the node with the largest id. At the end of a circle, the
circle initiator receives its own id and the largest id. The own id
indicates that he started the circle. The largest id indicates the
winner of the election.

ii. How many messages are passed for a leader election with the bully
algorithm

Each of the n nodes starts a circle of messages and one circle of
messages consists of n messages. This is n^^2 messages in total.

iii. The Bully Algorithm is an example for a leader election. Give
examples of applications that need a unique leader.

Databases often implement replication in the master/slave fashion. In
case the master dies, we need a failover. This is where the election
algorithm comes into play.

The lecture slides says:
– Determining the monitor station of Token-Ring-LANs
– Generating a unique token
– Determining the root node of a spanning tree
– Determining the master in distributed files systems or centralized mutual
exclusion

#### Exercise 1.2: Election

##### Questions

i. There is a precondition for the algorithm of Chang and Roberts that
all node IDs have to be unique (no duplicate IDs). Assume, we drop
this precondition and allow multiple nodes to have the same ID.

1. Does the algorithm still work properly? Please provide a reasonable
   answer.

2. In which cases does the algorithm still deliver a proper result?
Explain at least two cases.

ii. Derive the time complexity of the Hirschberg-Sinclair election
algorithm in the unite time complexity model. (You can assume that all
nodes initiate the algorithm at time t0 = 0)

#### Implementations

i. Implement the algorithm of Chang and Roberts that have been
introduced in the lecture. Afterwards evaluate the message complexity
of your implementation compared to the formulas provided in the
lecture given the following scenarios:

1. Worst-Case: Configure your topology that it reflects the worst-case
scenario.

2. Best-Case: Configure your topology that it reflects the best-case
   scenario.

3. Average-Case: Configure your topology that it randomly assigns node
IDs and examine the average message complexity calculated over several
runs.

ii. Implement the Hirschberg-Sinclair-Election Algorithm on
bidirectional rings.  Evaluate your implementation with a ring of 16
nodes and a ring of 17 nodes


#### Exercise 1.3: Mutual Exclusion

##### Questions

i. Lamport

The broadcast algorithm (Lamport, 1978) has been introduced in the
lecture. The algorithm requires FIFO channels. Assume, we drop this
precondition. Construct an example in which the algorithm does not
work properly anymore.

ii. Ricart and Agrawala

1. Is this algorithm deadlock-free? Give a reasonable answer.

2. Modify the broadcast algorithm of Ricart and Agrawala such that (at
maxi- mum) k2N processes are able to enter the critical section
instead of just one.

iii. Maekawa

1. The process mesh-algorithm (Maekawa, 1985) is based on the
assumption that n processes are arranged in a quadratic mesh with an
edge length of n. Consider a situation where this assumption is not
given (n is not a square number). Is it still feasible to use the
algorithm?


Additional notes and assessment:

* important parts of the implementation have to be annotated with
  comments

* each exercise has to be completed handled in teams of 3-4 students

* the exercise sheet is successfully completed, if exercise 2 was
presented and the solution was explained satisfactorily
