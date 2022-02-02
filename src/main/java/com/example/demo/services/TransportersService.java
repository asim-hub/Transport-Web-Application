package com.example.demo.services;

import com.example.demo.models.Transporters;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class TransportersService {
    public String createTransporter(Transporters transporter) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture =  db.collection("Transporters").document(transporter.getId().toString()).set(transporter);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String updateTransporter(Transporters transporter, Boolean add) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        if (add) {
            transporter.setNrFreeTrucks(transporter.getNrFreeTrucks() + 1);
            transporter.setNrTotalTrucks(transporter.getNrTotalTrucks() + 1);
        } else {
            transporter.setNrFreeTrucks(transporter.getNrFreeTrucks() - 1);
            transporter.setNrTotalTrucks(transporter.getNrTotalTrucks() - 1);
        }

        ApiFuture<WriteResult> collectionsApiFuture = db.collection("Transporters").document(transporter.getId().toString()).set(transporter);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteTransporter(String documentId) {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Transporters").document(documentId).delete();
        return "Successfully deleted" + documentId;
    }

    public Transporters getTransporter(String documentId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Transporters").document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Transporters transporter;

        if (document.exists()) {
            transporter = document.toObject(Transporters.class);
            return transporter;
        }

        return null;
    }
}
