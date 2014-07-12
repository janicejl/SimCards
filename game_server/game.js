
var io;
var gameSocket;

exports.initGame = function(sio, socket) {
  io = sio;
  gameSocket = socket;
  gameSocket.emit('connected', { message : 'You are connected.'} );

  gameSocket.on('playerConnected', playerConnected);
};

function playerConnected() {
  console.log('asdf');
  gameSocket.emit('secondMessage');
}
