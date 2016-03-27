#!/usr/bin/python3.4 -B

import sys

class Neuron:
	def __init__(self, data):
		self.name = data[0]
		self.activity = 0.0
		self.isMin = False
		self.isOutput = False
		for i in range(1, len(data)):
			if data[i].startswith("min="):
				self.isMin = True
				self.minActivity = float(data[i][4:])
			elif data[i].startswith("out="):
				self.isOutput = True
				self.output = data[i][4:]
	def onActivated(self):
		if self.isOutput:
			print("\"" + self.output + "\"")
	def applyActivity(self, activity):
		if self.isMin:
			if activity >= self.minActivity:
				self.activity = activity
				self.onActivated()
			else:
				self.activity = 0.0
		else:
			self.onActivated()
			self.activity = activity

class Connection:
	def __init__(self, data):
		self.activity = 0.0
		self.senderName = data[0]
		self.receiverName = data[1]
		self.weight = float(data[2])

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
				data = line[7:].split(", ")
				web.addNeuron(Neuron(data))
			elif line.startswith("connection "):
				data = line[11:].split(", ")
				web.addConnection(Connection(data))
			else:
				print("can't interpret line \"" + line + "\"")
		return web
	def toString(self):
		string = str()
		for neuron in self.neurons:
			string += neuron.name + "(" + str(neuron.activity) + "): "
			for connection in self.connections:
				if connection.senderName == neuron.name:
					string += "(" + connection.receiverName + ", " + str(connection.weight) + ")"
			string += "\n"
		return string.strip("\n")
	def getNeuron(self, name):
		for neuron in self.neurons:
			if neuron.name == name:
				return neuron
	def tick(self):
		for connection in self.connections:
			connection.activity = connection.weight * self.getNeuron(connection.senderName).activity
		for neuron in self.neurons:
			tmpAct = 0.0
			for connection in self.connections:
				if connection.receiverName == neuron.name:
					tmpAct += connection.activity
			neuron.applyActivity(tmpAct)
		for connection in self.connections: # could be removed
			connection.activity = 0.0
	def run(self):
		print("<Enter> to go one step forward")
		print("<neuron> = <activity> to set the activity\n")
		print("PRE:\n")
		print(self.toString())
		print("\nSTART:")
		try:
			while True:
				i = input("\n>> ")
				if i != "":
					tmp = i.replace(" ", "").split("=")
					self.getNeuron(tmp[0]).activity = float(tmp[1])
				else:
					self.tick()
				print(self.toString())
		except:
			print("Exited ...")
