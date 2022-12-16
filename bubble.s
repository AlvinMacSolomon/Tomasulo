--R3 = Size of Array in bytes
--R4 = Address of beginning of array 
--R5 = first address past array, for loop termination
--R6 = current iteration  (loop i variable)
--R7 = current array data value
--R8 = next array data value
--R9 = comparison Value
--R10 = current address being worked on (loop j variable)
--R11 = Address inner loop needs to run until
--R20 = 4 (for later use)
Begin Assembly
-- Data is at Org 4000
ADDI R4, R0, 4000
--Set Register 20 to be integer value 4
ADDI R20, R0, 4
-- Load number of elements
LW R2, 0(R4)
-- Multiply this by 4, since each element is 4 bytes
SLL R3, R2, 2
-- R4 is address of beginning of array of numbers
ADDI R4, R4, 4
-- R5 now points to first address past array
ADD R5, R4, R3
-- initialize loop variable to first address (4004)
ADD R6, R4, R0
-- R11 now points to first address past array
ADD R11, R4, R3
-----------------
LABEL LoopStart
BEQ R6, R5, PostLoop
ADDI R6, R6, 4
SUB R11, R11, R20
-- initialize j loop variable to first address (4004)
ADD R10, R4, R0
LABEL InnerLoop
BEQ R10, R11, LoopStart
LW R7, 0(R10)
LW R8, 4(R10)
SUB R9, R7, R8
ADDI R10, R10, 4
BGTZ R9, Swap
J InnerLoop
---
LABEL Swap
SW R7, 0(R10)
SW R8, -4(R10)
J InnerLoop
LABEL PostLoop
HALT
End Assembly
--Data Stuff
Begin Data 4000 32
150
76
54
46
92
91
88
26
34
75
22
22
46
84
1
64
72
29
96
38
25
87
57
81
98
17
64
9
59
86
7
17
1
7
73
36
26
40
48
7
20
31
64
71
4
55
35
64
78
85
81
51
77
44
91
78
85
1
57
50
48
17
59
32
40
97
23
95
15
25
95
40
90
7
96
50
84
71
89
98
4
45
81
55
65
69
58
77
45
46
97
86
10
38
80
67
91
23
94
34
58
74
33
17
47
44
80
86
49
93
62
51
26
80
69
6
97
45
65
36
76
6
58
89
98
96
8
97
47
5
73
77
92
29
4
53
59
78
16
27
22
83
76
38
62
96
54
5
88
46
60
End Data
