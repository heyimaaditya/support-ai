from vector_store import VectorStore

store = VectorStore()


print("Indexing articles...")
store.upsert_kb_article(
    "art_1", 
    "Our refund policy allows returns within 30 days with a receipt.", 
    "tenant_1"
)
store.upsert_kb_article(
    "art_2", 
    "To reset your password, click the forgot password link on the login page.", 
    "tenant_1"
)


print("Searching for 'How can I get my money back?'")
results = store.search_similar("How can I get my money back?", "tenant_1")

for match in results['matches']:
    print(f"Score: {match['score']:.4f} | Text: {match['metadata']['text']}")