package com.lms.demo;

import java.util.Scanner;

public class StockChangeCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueCalculation = true;

        System.out.println("股票涨幅计算器");
        System.out.println("================");

        while (continueCalculation) {
            try {
                // 输入两个价格
                System.out.print("\n请输入当前价格: ");
                double referencePrice = scanner.nextDouble();

                System.out.print("请输入目标价格: ");
                double currentPrice = scanner.nextDouble();

                // 验证参考价格不为0，避免除以0的错误
                if (Math.abs(referencePrice) < 1e-10) {
                    System.out.println("错误：参考价格不能为0，请重新输入。");
                    continue;
                }

                // 计算涨幅：((当前价格 - 参考价格) / 参考价格) * 100%
                double change = ((currentPrice - referencePrice) / referencePrice) * 100;

                // 输出结果，保留两位小数
                System.out.printf("涨幅: %.2f%%\n", change);

                // 根据涨幅给出提示信息
                if (change > 0) {
                    System.out.println("股票上涨↑");
                } else if (change < 0) {
                    System.out.println("股票下跌↓");
                } else {
                    System.out.println("价格持平→");
                }

            } catch (Exception e) {
                System.out.println("输入错误，请输入有效的数字。");
                scanner.nextLine(); // 清除错误的输入
                continue;
            }

            // 询问是否继续
            System.out.print("\n是否继续计算？(y/n): ");
            String choice = scanner.next().toLowerCase();

            if (!choice.equals("y") && !choice.equals("yes")) {
                continueCalculation = false;
            }
        }

        System.out.println("\n谢谢使用股票涨幅计算器！");
        scanner.close();
    }
}