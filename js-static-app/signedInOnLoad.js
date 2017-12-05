function signedInOnLoad( profile, id_token ) {
  sub( {
    onMessage: function ( message, ack_callback ) {
      /**
       * message: receivedMessages[].message.data base64decoded and json parsed
       * ack_callback: invoke to ack
       */
      console.log( message );
      ack_callback();
    }
  } )
}

function sub( params ) {
  setTimeout( function () {
    $.ajax( {
      url        : 'https://content-pubsub.googleapis.com/v1/projects/growbit-0/subscriptions/oraclize:pull?key=AIzaSyCJMUuleDg1fa-AmRbTE4f0aG7aXf-Yj7w&alt=json',
      method     : 'POST',
      data       : JSON.stringify( {
        maxMessages      : 5,
        returnImmediately: true
      } ),
      contentType: 'application/json; charset=UTF-8',
      dataType   : 'json',
      error      : function ( jqXHR, textStatus, errorThrown ) {
        console.log( 'error during sub pull' );
        console.log( textStatus );
        console.log( errorThrown );
        sub( params );
      },
      success    : function ( data, textStatus, jqXHR ) {
        if ( data.receivedMessages ) {
          data.receivedMessages.forEach( function ( receivedMessage ) {
            var message = atob( receivedMessage.message.data );
            params.onMessage( JSON.parse( message ), ack( receivedMessage.ackId ) )
          } )
        } else {
          console.log( 'no new message on this subscription' )
        }
        sub( params );
      },
      headers    : {
        'Authorization': 'Bearer ya29.c.ElwZBVhfNjCbZzirAkTAz37tCOnC-HcRLu96rIzLCNisc6pALthCN6kXJwokfWl7e36hEVpXb0yOULb-cewD6JY5Dpfdh-0JSqtKuMyi2CYVhr0bOz9K0SvILJTBTw'
      }
    } );
  }, 3000 );
}

function ack( ackId ) {
  return function () {
    console.log( "TBD ack this " + ackId );
  }
}

function pub() {
  console.log( "TBD pub " );
}