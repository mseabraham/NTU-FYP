import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
admin.initializeApp();

const db = admin.database();
//CREATE A NEW CLIENT USER FROM A TRAINER ACCOUNT 
 export const mkUser = functions.https.onCall(async (data, context) =>{
   try {
    const userRecord = await admin.auth().createUser({
      email: data.email,
      emailVerified: false,
      password: data.password,
      displayName: data.displayName,
      disabled: false
    })
    console.log('Successfully created new user:', userRecord.uid);

    await admin.auth().setCustomUserClaims(userRecord.uid, {
      role: "client",
      trainerId: data.trainerId

    })

    const clientRef = db.ref(`Users/${data.trainerId}/Clients`);
    await clientRef.update({
      [userRecord.uid] : {
        username: data.displayName,
        img: "default"
      }
    })
    return "OK"
   } catch (error) {
    console.log('Error creating new user:', error);
    return "ERROR"
   }
 
});

export const delUser = functions.https.onCall((data, context) =>{
  admin.auth().deleteUser(data.uid)
  .then(function() {
    console.log('Successfully deleted user');
  })
  .catch(function(error) {
    console.log('Error deleting user:', error);
  });
})

