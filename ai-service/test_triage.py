from agents.triage_agent import TriageAgent

agent = TriageAgent()


print("Testing Case 1: Angry Billing...")
res1 = agent.classify_ticket(
    "Where is my money??", 
    "I was charged twice for my subscription and I want a refund NOW. This is unacceptable."
)
print(f"Result: {res1}\n")


print("Testing Case 2: Technical Bug...")
res2 = agent.classify_ticket(
    "App crashes on startup", 
    "Every time I open the mobile app on my iPhone 15, it closes immediately."
)
print(f"Result: {res2}\n")


print("Testing Case 3: Spam...")
res3 = agent.classify_ticket(
    "Cheap watches for sale", 
    "Visit our website to get 90% off Rolex watches!"
)
print(f"Result: {res3}\n")