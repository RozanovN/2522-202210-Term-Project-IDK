
package ca.bcit.comp2522.termproject.idk;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

    public class UserLogIn extends Application {

        Connection conn;
        PreparedStatement pst = null;
        ResultSet rs = null;

        @Override
        public void start(Stage primaryStage) throws Exception
        {
            //GUIS a = new GUIS();
            //a.createConnection();
            //a.display();
            UserLogIn d = new UserLogIn();
            d.createConnection();

            primaryStage.setTitle("Retrive Database Values Into CheckBox");

            //primaryStage.getIcons().add(new Image("file:user-icon.png"));
            BorderPane layout = new BorderPane();
            Scene newscene = new Scene(layout, 1200, 700, Color.rgb(0, 0, 0, 0));

            Group root = new Group();
            Scene scene = new Scene(root, 320, 200, Color.rgb(0, 0, 0, 0));
            scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

            Color foreground = Color.rgb(255, 255, 255, 0.9);

            //Rectangila Background
            Rectangle background = new Rectangle(320, 250);
            background.setX(0);
            background.setY(0);
            background.setArcHeight(15);
            background.setArcWidth(15);
            background.setFill(Color.rgb(0 ,0 , 0, 0.55));
            background.setStroke(foreground);
            background.setStrokeWidth(1.5);

            VBox vbox = new VBox(5);
            vbox.setPadding(new Insets(10,0,0,10));

            Label label = new Label("Label");
            //label.setTextFill(Color.WHITESMOKE);
            label.setFont(new Font("SanSerif", 20));

            TextField username = new TextField();
            username.setFont(Font.font("SanSerif", 20));
            username.setPromptText("Username");
            username.getStyleClass().add("field-background");

            PasswordField password =new PasswordField();
            password.setFont(Font.font("SanSerif", 20));
            password.setPromptText("Password");
            password.getStyleClass().add("field-background");

            Button btn = new Button("Login");
            btn.setFont(Font.font("SanSerif", 15));
            btn.setOnAction(e ->{
                try{
                    String user = username.getText();
                    String pass = password.getText();
                    String query = "SELECT * FROM userdatabase.userdatabasetable Where username = '"+user+"' AND password = '"+pass+"' ";
                    pst = conn.prepareStatement(query);
                    pst.setString(1,user);
                    pst.setString(2,pass);
                    rs = pst.executeQuery();

                    if(rs.next()){
                        label.setText("Login Successful");
                        primaryStage.setScene(newscene);
                        primaryStage.show();
                    }else{
                        label.setText("Login Failed");
                    }
                    username.clear();
                    password.clear();
                    pst.close();
                    rs.close();
                }catch(Exception e1){
                    label.setText("SQL Error");
                    System.out.println("Wrong UserName Or Password");
                }
            });

            vbox.getChildren().addAll(label, username, password, btn);
            root.getChildren().addAll(background, vbox);

            primaryStage.setScene(scene);
            primaryStage.show();
        }


        Connection createConnection ()
        {
            try
            {
                //Class.forName("com.mysql.jdbc.Driver");
                Class.forName("com.mysql.cj.jdbc.Driver");


                final String URL = "jdbc:mysql://localhost:3306/comp2522";

                // We need to send a user and a password when we try to connect!
                final Properties connectionProperties = new Properties();
                connectionProperties.put("user", "root");
                connectionProperties.put("password", "root");

                Connection con = DriverManager.getConnection(URL, connectionProperties);
                System.out.println("DataBase Connected Successfully");
                Statement stmt = con.createStatement();


                // Execute the statement and receive the result...
                ResultSet rs = stmt.executeQuery("SELECT * FROM users");

//        Add another user


                // And then display the result!
                System.out.println("user_id\t\tpassword");
                while (rs.next()) {
                    String userID = rs.getString("user_id");
                    String password = rs.getString("password");
                    System.out.println(userID + "\t\t" + password);
                }

                con.close();
            }
            catch (ClassNotFoundException | SQLException ex)
            {
                Logger.getLogger(UserLogIn.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }


    }

