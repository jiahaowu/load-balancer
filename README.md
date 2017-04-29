CMSC714 FINAL PROJECT (load-balancer)

Kyunghun Lee, Jiahao Wu, Honglei Li

Project Proposal:
https://docs.google.com/document/d/19_nKuIUnWmFa45J4n_OchZZAkABrmB1bPaExUJ56Vss/edit?usp=sharing

Slang:
http://dspcad.slack.com

Important dates:

interim report - April 21, 6PM
https://docs.google.com/document/d/1B6i5bq-_xxUyA7HKqG2qAewin-0cMqh3RB6zLT0IPtQ/edit?usp=sharing

demos - May 4 and 9, in class

final report - due May 12, 6PM



Final Study

https://docs.google.com/document/d/1QzuN0ngutUoS5yN4Mhv1FWRd4GGlc7I1rAJ93VoK2Bc/edit?usp=sharing


Professor Feedback (for interim report)

I have to say I have some concerns about your progress on the project.  First, it's very late to not have anything implemented.  I think you will all have to do a lot of work on this over the next couple weeks.

Second, your benchmarking for machine performance is very simplistic.  I truly doubt that real applications would see anything like the performance your benchmark will show, so the load balancing may not work very well because you'll start from poor assumptions about node performance.  There are standard benchmarks out there for machine performance (e.g., HPL that is used for the TOP500, the graph algorithms used for the GRAPH500, etc.), so maybe you should think about using one of those instead.

I'm also not clear on how you handle failures.  If a node fails, how will that be detected, and who will report it?  Is the coordinator pinging each machine periodically to see if it is alive?  And what happens if the coordinator fails?  Lots of other questions here, but maybe you can at least tell me exactly what you are doing now.

Finally, how are you going to evaluate your load balancing system?  Are you going to apply a synthetic workload of some kind, and then see how well it does for load balancing?
