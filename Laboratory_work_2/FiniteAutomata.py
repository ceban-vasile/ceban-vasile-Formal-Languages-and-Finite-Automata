import networkx as nx
import matplotlib.pyplot as plt

class FiniteAutomaton:
    def __init__(self, states, alphabet, transitions, initial_state, accepting_states):
        self.states = states
        self.alphabet = alphabet
        self.transitions = transitions
        self.initial_state = initial_state
        self.accepting_states = accepting_states

    def is_deterministic(self):
        seen_transitions = set()
        for state, symbol in self.transitions.keys():
            if (state, symbol) in seen_transitions:
                return False
            seen_transitions.add((state, symbol))
        return True

    def convert_to_regular_grammar(self):
        grammar = {}
        for state, symbol in self.transitions.keys():
            destination = self.transitions[(state, symbol)]
            if (state, destination) not in grammar:
                grammar[(state, destination)] = []
            grammar[(state, destination)].append(symbol)

        return grammar

    def convert_to_dfa(self):
        if self.is_deterministic():
            return self  # Already a DFA

        dfa_states = set()
        dfa_transitions = {}
        dfa_initial_state = self.initial_state
        dfa_accepting_states = set()

        queue = [self.epsilon_closure([self.initial_state])]
        visited = set()

        while queue:
            current_states = queue.pop(0)
            current_states = tuple(sorted(current_states))

            if current_states in visited:
                continue
            visited.add(current_states)

            dfa_states.add(current_states)

            for symbol in self.alphabet:
                next_states = set()
                for state in current_states:
                    if (state, symbol) in self.transitions:
                        next_states.update(self.epsilon_closure([self.transitions[(state, symbol)]]))
                next_states = tuple(sorted(next_states))

                if not next_states:
                    continue

                dfa_transitions[(current_states, symbol)] = next_states

                if next_states not in dfa_states:
                    queue.append(next_states)

                if any(state in self.accepting_states for state in next_states):
                    dfa_accepting_states.add(next_states)

        return FiniteAutomaton(dfa_states, self.alphabet, dfa_transitions, dfa_initial_state, dfa_accepting_states)

    def epsilon_closure(self, states):
        epsilon_closure_states = set(states)
        queue = list(states)

        while queue:
            current_state = queue.pop(0)
            epsilon_transitions = self.transitions.get((current_state, ''), [])
            for state in epsilon_transitions:
                if state not in epsilon_closure_states:
                    epsilon_closure_states.add(state)
                    queue.append(state)

        return list(epsilon_closure_states)

    def draw_graph(self):
        G = nx.DiGraph()

        for state in self.states:
            G.add_node(state, shape='circle', color='blue')
            if state in self.accepting_states:
                G.nodes[state]['color'] = 'green'  # Accepting state

        for (source, symbol), destination in self.transitions.items():
            G.add_edge(source, destination, label=symbol)

        pos = nx.spring_layout(G)
        node_colors = [G.nodes[state]['color'] for state in G.nodes]
        edge_labels = {(source, destination): symbol for (source, symbol), destination in self.transitions.items()}

        nx.draw_networkx_nodes(G, pos, node_color=node_colors)
        nx.draw_networkx_edges(G, pos)
        nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels)
        nx.draw_networkx_labels(G, pos)

        plt.show()


# Main client class
if __name__ == "__main__":
    # Finite automaton definition
    states = {'q0', 'q1', 'q2', 'q3', 'q4'}
    alphabet = {'a', 'b', 'c'}
    transitions = {('q0', 'a'): 'q1', ('q1', 'b'): 'q2', ('q1', 'b'): 'q3',
                   ('q2', 'c'): 'q3', ('q3', 'a'): 'q3', ('q3', 'b'): 'q4'}
    initial_state = 'q0'
    accepting_states = {'q4'}

    fa = FiniteAutomaton(states, alphabet, transitions, initial_state, accepting_states)

    # a. Convert to regular grammar
    print("a. Convert to regular grammar")
    regular_grammar = fa.convert_to_regular_grammar()
    print("Regular Grammar:", regular_grammar)
    print(" ")

    # b. Determine whether the FA is deterministic or non-deterministic
    print("b. Determine whether the FA is deterministic or non-deterministic")
    if fa.is_deterministic():
        print("The Finite Automaton is deterministic.")
    else:
        print("The Finite Automaton is non-deterministic.")
    print(" ")

    # c. Convert NDFA to DFA
    print("c. Convert NDFA to DFA")
    dfa = fa.convert_to_dfa()
    print("DFA States:", dfa.states)
    print("DFA Transitions:", dfa.transitions)
    print("DFA Initial State:", dfa.initial_state)
    print("DFA Accepting States:", dfa.accepting_states)

    # d. Represent the finite automaton graphically (Optional)
    fa.draw_graph()
