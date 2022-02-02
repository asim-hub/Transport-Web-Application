package com.example.demo.services;

import com.example.demo.models.Requests;
import com.example.demo.models.Senders;
import com.example.demo.models.Transporters;
import com.example.demo.models.Trucks;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RequestsService {
    @Autowired
    private CountersService countersService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private SendersService sendersService;

    public String createRequest(Requests request) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        int count = countersService.getCounter("countR");
        request.setId(count + 1);

        // get id for current user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id_sender = usersService.getUser(userDetails.getUsername()).getId();
        request.setId_sender(id_sender);

        // update counter for trucks
        countersService.updateCounter("countR");

        ApiFuture<WriteResult> collectionsApiFuture =  db.collection("Requests").document(request.getId().toString()).set(request);

        CollectionReference requests = db.collection("Senders");
        Query query = requests.whereEqualTo("id_user", id_sender);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().size() != 0) {
            DocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
            Senders sender = document.toObject(Senders.class);

            // increase the total number of requests
            sendersService.updateSender(sender, true);
        }

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String updateRequest(Requests request) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = db.collection("Requests").document(request.getId().toString()).set(request);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteRequest(String documentId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Requests").document(documentId).delete();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id_sender = usersService.getUser(userDetails.getUsername()).getId();

        CollectionReference requests = db.collection("Senders");
        Query query = requests.whereEqualTo("id_user", id_sender);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().size() > 0) {
            DocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
            Senders sender = document.toObject(Senders.class);

            // decrease the total number of requests
            sendersService.updateSender(sender, false);
        }

        return "Successfully deleted" + documentId;
    }

    public Requests getRequest(String documentId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Requests").document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Requests request;
        if(document.exists()) {
            request = document.toObject(Requests.class);
            return request;
        }
        return null;
    }

    public List<Requests> getRequests(Integer id_sender) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference requests = db.collection("Requests");
        Query query = requests.whereEqualTo("id_sender", id_sender);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<Requests> requestsList = new ArrayList<>();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Requests request;
            request = document.toObject(Requests.class);
            requestsList.add(request);
        }

        return requestsList;
    }

    public List<Requests> getAllRequests() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("Requests").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Requests> requestsList = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            requestsList.add(document.toObject(Requests.class));
        }

        return requestsList;
    }
}
