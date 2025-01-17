package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "chats")
public class Chat extends AbstractEntity {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
//	@NotNull(message = "ユーザは必須入力です")//入力制限で引っかかっているからコメントアウト
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "event_id")
	@NotNull(message = "イベントは必須入力です")
	private Event event;
	
	@Column(length = 255, nullable = false)
//	@NotNull(message = "本文は必須入力です")//StringであるからNotEmptyが使える
	@NotEmpty(message = "本文は必須入力です")//StringであるからNotEmptyが使える
	private String body;

}

