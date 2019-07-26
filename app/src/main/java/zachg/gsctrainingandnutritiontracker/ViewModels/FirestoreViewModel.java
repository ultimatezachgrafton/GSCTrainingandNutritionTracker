package zachg.gsctrainingandnutritiontracker.ViewModels;

import androidx.lifecycle.ViewModel;

public class FirestoreViewModel extends ViewModel {
//    val TAG = "FIRESTORE_VIEW_MODEL"
//    var firebaseRepository = FirestoreRepository()
//    var savedAddresses : MutableLiveData<List<AddressItem>> = MutableLiveData()
//
//    // save address to firebase
//    fun saveAddressToFirebase(addressItem: AddressItem){
//        firebaseRepository.saveAddressItem(addressItem).addOnFailureListener {
//            Log.e(TAG,"Failed to save Address!")
//        }
//    }
//
//    // get realtime updates from firebase regarding saved addresses
//    fun getSavedAddresses(): LiveData<List<AddressItem>>{
//        firebaseRepository.getSavedAddress().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
//        if (e != null) {
//            Log.w(TAG, "Listen failed.", e)
//            savedAddresses.value = null
//            return@EventListener
//        }
//
//        var savedAddressList : MutableList<AddressItem> = mutableListOf()
//        for (doc in value!!) {
//            var addressItem = doc.toObject(AddressItem::class.java)
//            savedAddressList.add(addressItem)
//        }
//        savedAddresses.value = savedAddressList
//        })
//
//        return savedAddresses
//    }
//
//    // delete an address from firebase
//    fun deleteAddress(addressItem: AddressItem){
//        firebaseRepository.deleteAddress(addressItem).addOnFailureListener {
//            Log.e(TAG,"Failed to delete Address")
//        }
//    }
}
