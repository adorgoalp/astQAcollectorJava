
public class QuestionAnswerModel {
	public String siteQuestionNo;
	public String question, answer,category;

	

	public QuestionAnswerModel(String siteQuestionNo, String question, String answer, String category) {
		this.siteQuestionNo = siteQuestionNo;
		this.question = question;
		this.answer = answer;
		this.category = category;
	}



	@Override
	public String toString() {
		return "SiteQuestionNo - " + siteQuestionNo + "\n" 
				+"Category - " + category +"\n"
				+ "Question - " + question + "\n" 
				+ "Answer - " + answer + "\n\n"
				;
	}
}
