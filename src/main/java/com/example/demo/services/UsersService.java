package com.example.demo.services;

import com.example.demo.models.Counters;
import com.example.demo.models.Senders;
import com.example.demo.models.Transporters;
import com.example.demo.models.Users;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UsersService {
    @Autowired
    private CountersService countersService;

    @Autowired
    private TransportersService transportersService;

    @Autowired
    private SendersService sendersService;

    public String createUser(Users user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        int count = countersService.getCounter("countU");
        user.setId(count + 1);
        ApiFuture<WriteResult> collectionsApiFuture =  db.collection("Users").document(user.getUsername()).set(user);

        countersService.updateCounter("countU");

        if (user.getType().equals("SENDER")) {
            countersService.updateCounter("countS");

            Senders sender = new Senders();
            count = countersService.getCounter("countS");
            sender.setId(count);
            sender.setId_user(user.getId());
            sender.setNrReq(0);
            sendersService.createSender(sender);
        } else if (user.getType().equals("TRANSPORTER")) {
            countersService.updateCounter("countTran");

            Transporters transporter = new Transporters();
            count = countersService.getCounter("countTran");
            transporter.setId(count);
            transporter.setId_user(user.getId());
            transporter.setNrFreeTrucks(0);
            transporter.setNrTotalTrucks(0);
            transportersService.createTransporter(transporter);
        }

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String updateUser(Users user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = db.collection("Users").document(user.getUsername()).set(user);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteUser(String username) {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Users").document(username).delete();
        return "Successfully deleted" + username;
    }

    public Users getUser(String username) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Users").document(username);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Users user;
        if(document.exists()) {
            user = document.toObject(Users.class);
            return user;
        }
        return null;
    }
}
