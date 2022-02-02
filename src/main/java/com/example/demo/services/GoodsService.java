package com.example.demo.services;

import com.example.demo.models.Goods;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class GoodsService {
    public String createGoods(Goods good) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture =  db.collection("Goods").document(good.getId().toString()).set(good);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String updateGoods(Goods good) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = db.collection("Goods").document(good.getId().toString()).set(good);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteGoods(String documentId) {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Goods").document(documentId).delete();
        return "Successfully deleted" + documentId;
    }

    public Goods getGoods(String documentId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Goods").document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Goods good;
        if(document.exists()) {
            good = document.toObject(Goods.class);
            return good;
        }
        return null;
    }
}
