package demo.cn.strivers.mybase.web.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name="c_user")
public class CUser {

	@Id
	@GeneratedValue
	private Long userId;
	private String trueName;
	private String address;
    private String telphone;
}
