#!/usr/bin/python3.4 -B

class Neuron:
	def __init__(self, name):
		self.name = name
		self.activity = 0
	def applyActivity(self, activity):
		self.activity = activity

class Connection:
	def __init__(self, sender, receiver, weigth):
		self.activity = 0
		self.senderName = sender
		self.receiverName = receiver
		self.weigth = weigth

class OutputNeuron(Neuron):
	def applyActivity(self, activity):
		self.activity = activity
		print(self.name + " now active")

class KnowingNeuralWeb:
	def __init__(self):
		self.neurons = list()
		self.connections = list()
	def addNeuron(self, neuron):
		self.neurons.append(neuron)
	def addConnection(self, connection):
		self.connections.append(connection)
	@staticmethod
	def loadFromFile(filename):
		web = KnowingNeuralWeb()
		f = open(filename)
		lines = f.readlines()
		f.close()
		for line in lines:
			line = line.strip("\n")
			if line.startswith("neuron "):
				web.addNeuron(Neuron(line[7:]))
			elif line.startswith("outputneuron "):
				web.addNeuron(OutputNeuron(line[13:]))
			elif line.startswith("connection "):
				tmp = line[11:].split(", ")
				sender = tmp[0]
				receiver = tmp[1]
				weigth = int(tmp[2])
				web.addConnection(Connection(sender, receiver, weigth))
			else:
				print("can't interpret line \"" + line + "\"")
		return web
	def toString(self):
		string = str()
		for neuron in self.neurons:
			string += neuron.name + "(" + str(neuron.activity) + "): "
			for connection in self.connections:
				if connection.senderName == neuron.name:
					string += "(" + connection.receiverName + ", " + str(connection.weigth) + ")"
			string += "\n"
		return string.strip("\n")
	def getNeuron(self, name):
		for neuron in self.neurons:
			if neuron.name == name:
				return neuron
	def tick(self):
		for connection in self.connections:
			connection.activity = connection.weigth * self.getNeuron(connection.senderName).activity
		for neuron in self.neurons:
			tmpAct = 0
			for connection in self.connections:
				if connection.receiverName == neuron.name:
					tmpAct += connection.activity
			neuron.applyActivity(tmpAct)
		for connection in self.connections: # could be removed
			connection.activity = 0
	def run(self):
		print(self.toString())
		while True:
			input("")
			self.tick()
			print(self.toString())