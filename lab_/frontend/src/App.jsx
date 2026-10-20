import { useState } from 'react'
import BandsTable from './BandsTable'
import SpecialOperations from './SpecialOperations'
import Directory from './Directory'
import { DIRECTORIES } from './directories'

export default function App() {
  const [screen, setScreen] = useState('bands')

  return (
    <div>
      <nav style={{ marginBottom: 12 }}>
        <button disabled={screen === 'bands'} onClick={() => setScreen('bands')}>группы</button>{' '}
        <button disabled={screen === 'special'} onClick={() => setScreen('special')}>доп. функции</button>
        {['albums', 'coordinates', 'persons', 'locations'].map(n => (
          <span key={n}>
            <button disabled={screen === n} onClick={() => setScreen(n)}>
              {DIRECTORIES[n].title}
            </button>{' '}
          </span>
        ))}
      </nav>

      {screen === 'bands' && <BandsTable />}
      {screen === 'special' && <SpecialOperations />}
      {['albums', 'coordinates', 'persons', 'locations'].includes(screen) &&
        <Directory name={screen} />}
    </div>
  )
}