# Distributed Algorithms - Exercise
Winter term 2017/18, Danh Le Phuoc & Qian Liu, ODS


## Exercise sheet 2


#### Exercise 1.1: Bully Algorithm

##### Questions

i. Describe the Bully Algorithm

The bully algorithm requires at least a unidirectional ring
topology. Every node wakes up either as an initiator or when it
receives a message. 
Each message is composed from two parameters. First parameter is the 
initiator ID, and the second parameter is the ID of the node, which
at the moment leads the election.
When the node receives the message, if compares it's ID with the first
parameter in the message. If there is a match, that indicates that a full
circle was made and the node can conclude one of the following: "I won" if 
second parameter in the message matches it's ID or "I lost" if the parameter 
differs from it's ID.
On the other hand, if the first parameter of the message differs from the node's ID,
it assigns the maximum value between it's ID and the second parameter to the message as new
second parameter, as shown on Figure 1(a)  
These messages are the starts of circular chains
among all the nodes. Within a circular chain of messages the algorithm
determines the node with the largest ID. At the end of a circle, the
circle initiator receives its own id and the largest id. The own id
indicates that he started the circle. The largest id indicates the
winner of the election.
Figure 1(a) depicts above-adressed message circle, while Figure 1(b) depicts the
block diagram of deciding algorithm.

$#@$#@$ add figure1-a and figure 1-b

ii. How many messages are passed for a leader election with the bully
algorithm

Each of the n nodes starts a circle of messages and one circle of
messages consists of n messages. This is n^^2 messages in total.
One of common optimization techniques is that each node only becomes
active if it's ID is higher one of those participating in the elections.
This has a positive influence on an average case because it reduces the number of
message circles. However, this does not optimize the worst case complexity of an algorithm.
Consider Figure 2. If the nodes are aranged in accending order with regards to their ID,
even optimized algorithm wit have O(N^2) complexity measure.

$#@#^% add figure 2

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

Moreover, election algorithms are used at system initialization, due to current leader failure,
or for reelecting previous leader after recovery. Leaders are required in a set of processes which are
modeled in single-master, multiple-slave fashion, where communication is broadcasted between master and slave. 

#### Exercise 1.2: Election

##### Questions

i. There is a precondition for the algorithm of Chang and Roberts that
all node IDs have to be unique (no duplicate IDs). Assume, we drop
this precondition and allow multiple nodes to have the same ID.

1. Does the algorithm still work properly? Please provide a reasonable
   answer.
The paper from Chang and Roberts suggests thet if a node receives a message with ID,
identical to its own, it assumes that it is the winner of the election. Consider the Figure 3(a), where node D assumes that it
won the election, even though he didn't participate. If we introduce a new rule that a node can't win if it didn't participate, we will solve this problem for Figure 3(a). On the other hand, a new problem arrises, shown on Figure 3(b). Having multiple nodes with same ID's can lead to a "draw problem". Namely, multiple nodes have the highest multiple winners have same ID, but the closest to the election initiator in topological sence is the actual winner. 

2. In which cases does the algorithm still deliver a proper result?
Explain at least two cases.

Figure 3(c) depicts the situation where having multiple nodes with same IDs assigned doesnt influence the final result of the
algorithm. In this case a node with higher ID will initiate elections and all lower IDs don't have the influence, regardless of the unique assignment.
Second, there can be a node with higher ID between the nodes with same IDs. In this situation, higher node would "break the draw". Assume that node node C on Figure 3(c) initiated the elections. Node D would win, regardless of the multiple assignments.
Another case would be to disregard the "draw problem". We can introduce the aditional constraints. If a node didn't participate in the elections, and receives a message with it's ID, that doesn't make him a leader. However, it forwards the message maintaining the flow. If we have a draw, the first node to receive it's number wins. With this, we can achieve that a lower node can't beat the higher node, and when there are multiple highest nodes in the graph, the winner is determined by relative distance to the elections initiator.

>>>figure 3<<<

ii. Derive the time complexity of the Hirschberg-Sinclair election
algorithm in the unite time complexity model. (You can assume that all
nodes initiate the algorithm at time t0 = 0)
We can estimate the best case and worst case, and based on worst case estimate complexity model.
In best case, nodes are aranged in descending order, as shown in Figure 4(a). This leads to each node, except first winning the right comparison, and losing the left comparison. The algorithm converges in one step, because only highest node wins both comparrisons.

On the other hand, the worst case complexity is the result of the folowing topology:
Nodes are aranged in such manner that two nodes with highest ID are as distant as possible in the topology. Recursively, next set of two nodes with highest ID are between thefirst two nodes at equal distance. this procedure results in a topology shown in Figure 4(b).
Result of this is the fact that in each iteration half of the remaining nodes remain in the election (node survives only if both neighbors don't survive). Inactive nodes only pass information to other active nodes.
With this in mind, we can estimate the time complexity. In our case, one time unit is a hop between two nodes.
In first iteration, nodes comapre through one hop. In second iteration, nodes compare in two hops. In third iteration, nodes compare in three hops. We can generalize the following : given each level k, the comparisons between nodes last k equal units of time. 
With this in mind, we can estimate the time as follows:
Time  = 2 * (1 + 2 + 3 + 4 + ... + n) < 2*(2n) = O(n)

This estimation can be generalized:
If we have log(n) comparison levels, with each level k lasting k units of time, we conclude that the time to send messages is bounded by O(2*log(n)*log(b)) = O(n). In this case, we haven't taken into account the time to receive echos. Since it is a pure multiplicative factor, the resulting bound O(2*2*log(n)*log(n)) still converges to O(n)


#### Implementations

i. Implement the algorithm of Chang and Roberts that have been
introduced in the lecture. Afterwards evaluate the message complexity
of your implementation compared to the formulas provided in the
lecture given the following scenarios:

1. Worst-Case: Configure your topology that it reflects the worst-case
scenario.

Worst case scenario is a ring topology where nodes are aranged in increasing order, because each node would reinitiate elections, as can be seen from the simulation with topology topo_worst.txt 

2. Best-Case: Configure your topology that it reflects the best-case
   scenario.

Best case scenario is the situation where ring is configured in the descending order. This implies that upon initiation, following nodes
will have ID's less then the initiator, whus wont reinitiate. This is exampled in simulation with topo_best.txt.

3. Average-Case: Configure your topology that it randomly assigns node
IDs and examine the average message complexity calculated over several
runs.

The complexity depends on the tradeof between ascending and descending order of nodes in the ring. Different ddscenarios result in different initial node configurations, as can be seen from simulation with topo_avg.txt

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

In the slides: message 6 includes the queue of process 1?

What is a lossless FIFO channel exactly? Does it either mean we have a
synchronized clock or does it mean we do not lose any message.

solution idea: lose a message vs. delay a message for a long time, so
that the synchronized clock is violated.

ii. Ricart and Agrawala

1. Is this algorithm deadlock-free? Give a reasonable answer.

It is deadlock-free because in case multiple nodes start with their
first message, the algorithms assumes a priority for the node with the
lowest id. So this is the tiebreaker otherwise we would have a
deadlock situation, in which each node would wait for a confirmation
and end in a deadlock.

2. Modify the broadcast algorithm of Ricart and Agrawala such that (at
maximum) 2 processes are able to enter the critical section instead of
just one.

The idea is as follows: When we change the required confirmations from
n to n-1, then two nodes at maximum are able to access the resource at
the same time.

iii. Maekawa

1. The process mesh-algorithm (Maekawa, 1985) is based on the
assumption that n processes are arranged in a quadratic mesh with an
edge length of n. Consider a situation where this assumption is not
given (n is not a square number). Is it still feasible to use the
algorithm?

The algorithm is still feasible in case it is not a quadratic
mesh. The assumption that all pairs of nodes have two processes in
common is still true. The disadvantage though in a non quadratic mesh
is that the granting set for each node becomes bigger and the
algorithm does not save that many messages.

Example: Consider a set of 16 nodes. For a quadratic mesh we would
have a granting set for each node of only 6 nodes. For a 8x2 mesh we
would have a granting set of 8 nodes.

Additional notes and assessment:

* important parts of the implementation have to be annotated with
  comments

* each exercise has to be completed handled in teams of 3-4 students

* the exercise sheet is successfully completed, if exercise 2 was
presented and the solution was explained satisfactorily
