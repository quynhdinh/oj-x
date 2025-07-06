-- drop table if exists submission;
-- drop table if exists leaderboard;
-- drop table if exists user;
-- drop table if exists contest;
-- drop table if exists test_case;
-- drop table if exists problem;
create table if not exists user (
    user_id int primary key auto_increment,
    user_type varchar(20) not null,
    user_name varchar(50) not null unique,
    password varchar(255) not null,
    email varchar(100) not null unique,
    name varchar(100),
    country varchar(50),
    rating int,
    unique (user_name)
);
insert into user (user_id, user_type, user_name, password, email, name, country, rating) values
(1, 'admin', 'admin', 'a', 'admin@ojx.com', 'Quynh', 'Vietnam', 1400),
(2, 'user', 'john_doe', 'password123', 'john@example.com', 'John Doe', 'USA', 1200),
(3, 'problem_setter', 'alice_smith', 'pass456', 'alice@example.com', 'Alice Smith', 'Canada', 1350),
(4, 'user', 'bob_wilson', 'mypass789', 'bob@example.com', 'Bob Wilson', 'UK', 1100),
(5, 'user', 'charlie_brown', 'charlie123', 'charlie@example.com', 'Charlie Brown', 'Australia', 950),
(6, 'user', 'diana_prince', 'diana456', 'diana@example.com', 'Diana Prince', 'Germany', 1280),
(7, 'user', 'eve_adams', 'eve789', 'eve@example.com', 'Eve Adams', 'France', 1150),
(8, 'user', 'frank_castle', 'frank123', 'frank@example.com', 'Frank Castle', 'Japan', 1320)
on duplicate key update user_id = values(user_id);

create table if not exists problem (
    problem_id int primary key auto_increment,
    problem_name varchar(100) not null,
    problem_statement text not null,
    difficulty varchar(20) not null,
    tags text,
    isVisible int default 1
);
insert into problem (problem_id, problem_name, problem_statement, difficulty, tags, isVisible) values
(1, 'Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume that each input would have exactly one solution, and you may not use the same element twice.', 'Easy', 'Array,Hash Table', 0),
(2, 'Add Two Numbers', 'You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.', 'Medium', 'Linked List,Math,Recursion', 0),
(3, 'Longest Substring Without Repeating Characters', 'Given a string s, find the length of the longest substring without repeating characters.', 'Medium', 'Hash Table,String,Sliding Window', 0),
(4, 'Median of Two Sorted Arrays', 'Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.', 'Hard', 'Array,Binary Search,Divide and Conquer', 0),
(5, 'Palindrome Number', 'Given an integer x, return true if x is palindrome integer. An integer is a palindrome when it reads the same backward as forward.', 'Easy', 'Math', 1),
(6, 'Reverse Integer', 'Given a signed 32-bit integer x, return x with its digits reversed. If reversing x causes the value to go outside the signed 32-bit integer range, then return 0.', 'Medium', 'Math', 1),
(7, 'String to Integer (atoi)', 'Implement the myAtoi(string s) function, which converts a string to a 32-bit signed integer (similar to C/C++''s atoi function).', 'Medium', 'String', 1),
(8, 'Container With Most Water', 'You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the ith line are at (i, 0) and (i, height[i]). Find two lines that together with the x-axis form a container, such that the container contains the most water.', 'Medium', 'Array,Two Pointers', 1),
(9, 'Valid Parentheses', 'Given a string s containing just the characters ''('', ''{'', ''['', '')'', ''}'', and '']'', determine if the input string is valid. An input string is valid if the brackets are closed in the correct order.', 'Easy', 'String,Stack', 1),
(10, 'Merge Two Sorted Lists', 'You are given the heads of two sorted linked lists list1 and list2. Merge the two lists in a one sorted list. The list should be made by splicing together the nodes of the first two lists.', 'Easy', 'Linked List,Recursion', 1),
(11, 'Maximum Subarray', 'Given an integer array nums, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.', 'Easy', 'Array,Divide and Conquer,Dynamic Programming', 1),
(12, 'Climbing Stairs', 'You are climbing a staircase. It takes n steps to reach the top. Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?', 'Easy', 'Dynamic Programming,Math', 1),
(13, 'Best Time to Buy and Sell Stock', 'You are given an array prices where prices[i] is the price of a given stock on the ith day. Find the maximum profit you can achieve from this transaction. You may complete at most one transaction (i.e., buy one and sell one share of the stock).', 'Easy', 'Array,Dynamic Programming', 1),
(14, 'Symmetric Tree', 'Given the root of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).', 'Easy', 'Tree,Breadth-First Search,Depth-First Search', 1),
(15, 'Binary Tree Level Order Traversal', 'Given the root of a binary tree, return the level order traversal of its nodes'' values. (i.e., from left to right, level by level).', 'Medium', 'Tree,Breadth-First Search', 1),
(16, 'Path Sum II', 'Given the root of a binary tree and an integer targetSum, return all root-to-leaf paths where each path''s sum equals targetSum.', 'Medium', 'Tree,Breadth-First Search,Depth-First Search', 1)
on duplicate key update problem_id = values(problem_id);

create table if not exists test_case (
    id int primary key auto_increment,
    problem_id int not null,
    input text not null,
    output text not null,
    is_sample int not null,
    foreign key (problem_id) references problem(problem_id)
);
insert into test_case (id, problem_id, input, output, is_sample) values
-- Test cases for Problem 1: Two Sum
(1, 1, '[2,7,11,15]\n9', '[0,1]', 1),
(2, 1, '[3,2,4]\n6', '[1,2]', 1),
(3, 1, '[3,3]\n6', '[0,1]', 0),
(4, 1, '[1,2,3,4,5]\n9', '[3,4]', 0),

-- Test cases for Problem 2: Add Two Numbers
(5, 2, '[2,4,3]\n[5,6,4]', '[7,0,8]', 1),
(6, 2, '[0]\n[0]', '[0]', 1),
(7, 2, '[9,9,9,9,9,9,9]\n[9,9,9,9]', '[8,9,9,9,0,0,0,1]', 0),

-- Test cases for Problem 3: Longest Substring Without Repeating Characters
(8, 3, 'abcabcbb', '3', 1),
(9, 3, 'bbbbb', '1', 1),
(10, 3, 'pwwkew', '3', 0),
(11, 3, '', '0', 0),

-- Test cases for Problem 4: Median of Two Sorted Arrays
(12, 4, '[1,3]\n[2]', '2.00000', 1),
(13, 4, '[1,2]\n[3,4]', '2.50000', 1),
(14, 4, '[]\n[1]', '1.00000', 0),

-- Test cases for Problem 5: Palindrome Number
(15, 5, '121', 'true', 1),
(16, 5, '-121', 'false', 1),
(17, 5, '10', 'false', 0),
(18, 5, '0', 'true', 0),

-- Test cases for Problem 6: Reverse Integer
(19, 6, '123', '321', 1),
(20, 6, '-123', '-321', 1),
(21, 6, '120', '21', 0),
(22, 6, '0', '0', 0),

-- Test cases for Problem 7: String to Integer (atoi)
(23, 7, '42', '42', 1),
(24, 7, '   -42', '-42', 1),
(25, 7, '4193 with words', '4193', 0),
(26, 7, 'words and 987', '0', 0),

-- Test cases for Problem 8: Container With Most Water
(27, 8, '[1,8,6,2,5,4,8,3,7]', '49', 1),
(28, 8, '[1,1]', '1', 1),
(29, 8, '[1,2,1]', '2', 0),
(30, 8, '[2,3,4,5,18,17,6]', '17', 0)
on duplicate key update
    problem_id = values(problem_id),
    input = values(input),
    output = values(output),
    is_sample = values(is_sample);

create table if not exists contest (
    contest_id int primary key auto_increment,
    contest_name varchar(100) not null,
    length int not null,
    started_at long not null,
    problem_ids text not null,
    points varchar(20) not null
);
insert into contest (contest_id, contest_name, length, started_at, problem_ids, points) values
(1, 'Weekly Contest 1', 120, 1633036800000, '1,2,3', '1,2,3'),
(2, 'Monthly Contest 1', 180, 1633123200000, '4,5,6', '1,2,3'),
(3, 'Yearly Contest 2023', 360, 1633219200000, '7,8,9', '1,2,3')
on DUPLICATE key UPDATE
    contest_id = values(contest_id);

create table if not exists submission (
    submission_id int primary key auto_increment,
    problem_id int not null,
    language varchar(20) not null,
    user_id int not null,
    source_code text not null,
    judge_status varchar(20) not null,
    created_at long not null,
    foreign key (user_id) references user(user_id),
    foreign key (problem_id) references problem(problem_id)
);