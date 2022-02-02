package com.example.demo.services;

import com.example.demo.models.Contract;
import com.example.demo.models.Trucks;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class ContractService {
    @Autowired
    private CountersService countersService;

    public String createContract(Contract contract) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        int count = countersService.getCounter("countC");
        contract.setId(count + 1);
        ApiFuture<WriteResult> collectionsApiFuture =  db.collection("Contract").document(contract.getId().toString()).set(contract);

        countersService.updateCounter("countC");

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String updateContract(Contract contract) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = db.collection("Contract").document(contract.getId().toString()).set(contract);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteContract(String documentId) {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Contract").document(documentId).delete();
        return "Successfully deleted" + documentId;
    }

    public Contract getContract(String documentId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Contract").document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Contract contract;
        if(document.exists()) {
            contract = document.toObject(Contract.class);
            return contract;
        }
        return null;
    }

    public List<Contract> getContracts(Integer idUser, String type) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference contracts = db.collection("Contract");

        Query query = null;

        if (type.equals("SENDER")) {
            query = contracts.whereEqualTo("id_sender", idUser);
        } else if(type.equals("TRANSPORTER")) {
            query = contracts.whereEqualTo("id_transporter", idUser);
        }

        ApiFuture<QuerySnapshot> querySnapshot = Objects.requireNonNull(query).get();
        List<Contract> contractList = new ArrayList<>();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Contract contract;
            contract = document.toObject(Contract.class);
            contractList.add(contract);
        }

        return contractList;
    }
}
