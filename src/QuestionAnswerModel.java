
public class QuestionAnswerModel {
	public int questionId;
	public String question, answer,category;

	

	public QuestionAnswerModel(int questionId, String question, String answer, String category) {
		this.questionId = questionId;
		this.question = question;
		this.answer = answer;
		this.category = category;
	}



	@Override
	public String toString() {
		return "ID - " + questionId + "\n" 
				+ "Question - " + question + "\n" 
				+ "Answer - " + answer + "\n"
				+"Category - " + category +"\n\n";
	}
}
