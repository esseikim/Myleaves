package com.itwill.myleaves.repository.qna;

import org.hibernate.annotations.ColumnDefault;

import com.itwill.myleaves.dto.qna.QnAUpdateDto;
import com.itwill.myleaves.repository.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@Entity
@Table(name = "INFO_QNA")
@SequenceGenerator(name = "INFO_QNA_SEQ_GEN" , sequenceName = "INFO_QNA_SEQ", allocationSize = 1)
public class QnA extends BaseTimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INFO_QNA_SEQ_GEN")
	private long qid;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String content;
	
	@Column(nullable = false)
	private String user_id;
	
	@ColumnDefault(value= "0")
	private long secret;
	
	
    public QnA update(QnAUpdateDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        
        return this;
    }

	
	/*
	 * @Column(nullable = false) private String an_title;
	 * 
	 * @Column(nullable = false) private String an_content;
	 * 
	 * @CreatedDate private LocalDateTime an_date;
	 */
	 
}