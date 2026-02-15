import os
from pinecone import Pinecone
from sentence_transformers import SentenceTransformer
from dotenv import load_dotenv

load_dotenv()

class VectorStore:
    def __init__(self):
        self.pc=Pinecone(api_key=os.getenv("PINECONE_API_KEY"))
        self.index=self.pc.Index("supportai-index")

        self.model=SentenceTransformer('all-MiniLM-L6-v2')

    def upsert_kb_article(self, article_id: str,text:str,tenant_id:str,metadata:dict=None):
        """Convert text to vector and store in Pinecone"""
        embedding = self.model.encode(text).tolist()
        

        payload = {
            "tenant_id": tenant_id,
            "text": text
        }
        if metadata:
            payload.update(metadata)

        self.index.upsert(
            vectors=[(article_id, embedding, payload)]
        )
        return True

    def search_similar(self, query: str, tenant_id: str, top_k: int = 3):
        """Search for relevant articles based on meaning"""
        query_vector = self.model.encode(query).tolist()
        
        results = self.index.query(
            vector=query_vector,
            top_k=top_k,
            filter={"tenant_id": {"$eq": tenant_id}},
            include_metadata=True
        )
        return results