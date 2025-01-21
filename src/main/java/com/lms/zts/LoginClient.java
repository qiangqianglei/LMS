package com.lms.zts;

import com.lms.utils.ZtsAuthUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class LoginClient {
        private JFrame jFrame = new JFrame("安全接入客户端");
        private Container c = jFrame.getContentPane();
        private JLabel a1 = new JLabel("用户名");
        private JTextField username = new JTextField();
        private JLabel a2 = new JLabel("密   码");
        private JPasswordField password = new JPasswordField();
        private JButton okbtn = new JButton("登录");

        public LoginClient() {
            // 设置窗体的位置及大小
            jFrame.setBounds(600, 200, 895, 595);
            // 设置一层相当于桌布的东西
            c.setLayout(new BorderLayout());//布局管理器
            // 设置按下右上角X号后关闭
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // 初始化--往窗体里放其他控件
            init();
            // 设置窗体可见
            jFrame.setVisible(true);

            listener();
        }
        public void init() {
            /*标题部分--North*/
            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new FlowLayout());
            titlePanel.add(new JLabel("登录获取更多内容"));
            c.add(titlePanel, "North");

            /*输入部分--Center*/
            BackgroundPanel backgroundPanel = new BackgroundPanel("src/main/resources/image/background.png");
            backgroundPanel.setLayout(null);
            a1.setBounds(305, 160, 50, 20);
            a2.setBounds(305, 200, 50, 20);
            backgroundPanel.add(a1);
            backgroundPanel.add(a2);
            username.setBounds(350, 160, 255, 30);
            password.setBounds(350, 200, 255, 30);
            backgroundPanel.add(username);
            backgroundPanel.add(password);

            // 登录按钮放到密码框下方
            okbtn.setBounds(350, 250, 255, 30); // 设置登录按钮的位置

            // 添加按钮
            backgroundPanel.add(okbtn);

            c.add(backgroundPanel, "Center");

        }
        //测试
        public static void main(String[] args) {
            new LoginClient();
        }

    public void listener() {
        // 确认按下去获取
        okbtn.addActionListener(e -> {
            String uname = username.getText();
            String pwd = String.valueOf(password.getPassword());
            try {
                ZtsAuthUtil.auth(uname, pwd);
                // 登录成功，关闭登录窗口并显示主界面
                jFrame.dispose();
                showMainPage();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    // 显示主界面的方法
    private void showMainPage() {
        JFrame mainFrame = new JFrame("主界面");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(895, 595);
        mainFrame.setLocationRelativeTo(null);

        BackgroundPanel mainPanel = new BackgroundPanel("src/main/resources/image/background.png");
        mainPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel(username.getText() +"您好，您已接入安全网络", SwingConstants.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setVisible(true);
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage(); // 加载背景图
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 重写paintComponent方法，绘制背景图
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}

