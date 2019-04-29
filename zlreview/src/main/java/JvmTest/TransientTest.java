package JvmTest;

import java.io.*;

/**
 * Created by hl on 2019/4/12.
 */
public class TransientTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        User user = new User();
        user.setPasswd("123456");
        user.setUsername("zl239");
        System.out.println("read before Serializable: ");
        System.out.println("username1: " + user.getUsername());
        System.err.println("password1: " + user.getPasswd());

        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("/Users/hl/IdeaProjects/4/nettyinaction/zlreview/src/main/java/JvmTest/javaObject.obj")
        );
        out.writeObject(user);
        out.flush();
        out.close();

        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("/Users/hl/IdeaProjects/4/nettyinaction/zlreview/src/main/java/JvmTest/javaObject.obj"));
        User u = (User) in.readObject();
        in.close();

        System.out.println("\nread after Serializable: ");
        System.out.println("username: " + u.getUsername());
        System.err.println("password: " + u.getPasswd());
    }

}

class User implements Serializable {

    private static final long serialVersionUID = 8294180014912103005L;

    private String username;
    private transient String passwd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}
