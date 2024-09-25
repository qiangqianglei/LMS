package com.lms.demo.leecode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Demo {


    /* 在数组的索引 index 处插入元素 num */
    void insert(int[] nums, int num, int index) {
        // 把索引 index 以及之后的所有元素向后移动一位
        for (int i = nums.length - 1; i > index; i--) {
            nums[i] = nums[i - 1];
        }
        // 将 num 赋给 index 处的元素
        nums[index] = num;
    }

    /* 删除索引 index 处的元素 */
    void remove(int[] nums, int index) {
        // 把索引 index 之后的所有元素向前移动一位
        for (int i = index; i < nums.length - 1; i++) {
            nums[i] = nums[i + 1];
        }
    }

    /* 链表节点类 */
    class ListNode {
        int val;        // 节点值
        ListNode next;  // 指向下一节点的引用
        ListNode(int x) { val = x; }  // 构造函数
    }

    /* 在链表的节点 n0 之后插入节点 P */
    void insert(ListNode n0, ListNode P) {
        ListNode n1 = n0.next;
        P.next = n1;
        n0.next = P;
    }

    /* 删除链表的节点 n0 之后的首个节点 */
    void remove(ListNode n0) {
        ListNode n1 = n0.next;
        if (n1 == null) return;

        ListNode n2 = n1.next;
        n0.next = n2;
    }

    public static void main(String[] args) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "username");
        jsonArray.add(jsonObject);
        System.out.println(JSON.toJSONString(jsonArray));
    }
}
