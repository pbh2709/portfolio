package com.ssafy.pjt.model.dto;

public class Review {
	private int reviewNo;//리뷰 번호
	private int videoNo; //게시물 번호 가져오기
	private String writerId; //유저 id 가져오기
	private String title; //리뷰 제목
	private String writer; //리뷰 작성자(id에서 가져오기)
	private String content;//리뷰 본문
	private String regDate;//리뷰 작성날짜

	public Review() {
	}

	public Review(String title, String writer, String content) {
		super();
		this.title = title;
		this.writer = writer;
		this.content = content;
	}

	

	public Review(int reviewNo, int videoNo, String writerId, String title, String writer, String content,
			String regDate) {
		super();
		this.reviewNo = reviewNo;
		this.videoNo = videoNo;
		this.writerId = writerId;
		this.title = title;
		this.writer = writer;
		this.content = content;
		this.regDate = regDate;
	}

	public int getReviewNo() {
		return reviewNo;
	}

	public void setReviewNo(int reviewNo) {
		this.reviewNo = reviewNo;
	}

	public int getVideoNo() {
		return videoNo;
	}

	public void setVideoNo(int videoNo) {
		this.videoNo = videoNo;
	}

	public String getWriterId() {
		return writerId;
	}

	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "Review [reviewNo=" + reviewNo + ", videoNo=" + videoNo + ", writerId=" + writerId + ", title=" + title
				+ ", writer=" + writer + ", content=" + content + ", regDate=" + regDate + "]";
	}


}
