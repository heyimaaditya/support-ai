from typing import Literal
from pydantic import BaseModel, Field
from langchain_groq import ChatGroq 
from langchain_core.prompts import ChatPromptTemplate
from dotenv import load_dotenv
import os

load_dotenv()

class TicketClassification(BaseModel):
    category: Literal["Technical", "Billing", "Account", "Feature Request", "Spam"] = Field(
        description="The category the ticket belongs to"
    )
    priority: Literal["Low", "Medium", "High", "Urgent"] = Field(
        description="The urgency of the ticket based on impact"
    )
    sentiment: Literal["Positive", "Neutral", "Negative", "Angry"] = Field(
        description="The emotional tone of the user"
    )
    reasoning: str = Field(description="Brief explanation of why this classification was chosen")

class TriageAgent:
    def __init__(self):

        self.llm = ChatGroq(
            model="llama-3.3-70b-versatile",
            temperature=0,
            groq_api_key=os.getenv("GROQ_API_KEY")
        )
        
        self.structured_llm = self.llm.with_structured_output(TicketClassification)

    def classify_ticket(self, title: str, description: str):
        prompt = ChatPromptTemplate.from_messages([
            ("system", "You are a senior customer support triage specialist. Analyze the ticket and provide a structured classification."),
            ("human", "Ticket Title: {title}\nTicket Description: {description}")
        ])

        chain = prompt | self.structured_llm
        try:
            return chain.invoke({"title": title, "description": description})
        except Exception as e:
            print(f"Error in TriageAgent: {e}")
            return None