package cn.eric.h2.repository;

import cn.eric.h2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: UserRepository
 * @Description: TODO
 * @company lsj
 * @date 2019/8/9 16:19
 **/
public interface UserRepository extends JpaRepository<User, Integer> {
}
