package DataStructures;

import org.junit.Test;

import java.util.Arrays;

public class Solution {

    public boolean search(int[] nums, int target) {
        if (nums.length == 0) {
            return false;
        }
        int left = 0;
        int right = nums.length - 1;
        // 使用二分查找找到最小元素的下标
        while (left < right) {
            int mid = (left + right) / 2;
            // 常规的，如果右边有序，则取右侧继续循环
            if (nums[mid] > nums[right]) {
                left = mid + 1;
                // 常规的，如果左侧有序，则取左侧继续循环（注意包含mid端点）
            } else if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                // 如果中间等于右侧，则存在四种情况、两类：
                // 例如：
                // 11101,11131。对应的是首节点在右侧，特征是：右侧存在非mid值
                // 10111,13111。对应首节点在左侧，特征是：右侧全是mid值
                boolean inRight = false;
                for (int i = mid; i <= right; i++) {
                    if (nums[i] != nums[mid]) {
                        inRight = true;
                        break;
                    }
                }
                if (inRight) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
        }
        int offset = left;
        int ans;
        // 使用二分计算真实的位置是否存在
        if (target >= nums[offset] && target <= nums[nums.length - 1]) {
            ans = Arrays.binarySearch(nums, offset, nums.length, target);
        } else {
            ans = Arrays.binarySearch(nums, 0, offset, target);
        }
        return ans >= 0;
    }

    @Test
    public void main() {
        int[] nums = {1};
        System.out.println(new Solution().search(nums, 1));
    }


}
