package cn.eric.h2.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: User
 * @Description: TODO
 * @company lsj
 * @date 2019/8/9 16:18
 **/
@Entity
@Table(name = "t_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String url;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
