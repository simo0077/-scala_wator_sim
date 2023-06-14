# Wa-Tor

Wa-Tor is a population dynamics simulation with two species: sharks and tunas. These species coexist in the same environment, which, in our case, represents the ocean. Tuna reproduce freely, while sharks aim to eat the tunas.

- `nTunas`: The initial number of tunas in the environment.
- `nSharks`: The initial number of sharks in the environment.

Each species has its own set of parameters:

## Tuna

For each tuna:

- `tBreed`: The number of cycles before a tuna can reproduce.

Tunas follow these rules:

1. Each cycle, it moves randomly to a neighboring empty cell in the Moore neighborhood.
2. If there are no free cells, it doesn't move and it doesn't reproduce.
3. After `tBreed` cycles, it reproduces, giving birth to a new tuna in the cell it occupied before moving.

## Sharks

For each shark:

- `sBreed`: The number of cycles before a shark can reproduce.
- `sEnergy`: The energy level of the shark. This increases when it eats a fish and decreases when it doesn't.

Sharks follow these rules:

1. Each cycle, it moves to a neighboring cell occupied by a tuna. If no such cell is available, it moves to a neighboring free cell.
2. If it eats a tuna, it gains one point of `sEnergy`.
3. If there are no cells occupied by a tuna or free cells, it doesn't move and it doesn't reproduce.
4. After `sBreed` cycles, it reproduces, giving birth to a new shark in the cell it occupied before moving.
5. If it doesn't eat a tuna after `sEnergy` cycles, it dies.

This simulation is implemented using `JFXApp3`.
